package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;
import nl.colinrosen.sockets.api.server.events.client.ClientConnectEvent;
import nl.colinrosen.sockets.api.server.events.client.ClientNotificationEvent;
import nl.colinrosen.sockets.api.server.events.packets.PacketReceiveEvent;
import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.shared.packets.CRPacketIn;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming.PacketInConnectedXXNotification;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming.PacketInHandShake00Response;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutAny00Disconnect;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutConnectedXXNotification;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutHandShake00HandShake;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutHandshake01Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public class CRConnection implements Connection, Runnable {

    private CRServer server;
    private Socket socket;
    private UUID id;
    private BufferedReader in;
    private BufferedWriter out;
    private long timeout = 26000;
    private ClientPing ping;
    private boolean connected;
    private boolean closed;
    private double handshakeResult;

    public CRConnection(CRServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        ping = new ClientPing(this);
        id = UUID.randomUUID();

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread th = new Thread(this);
        th.start();

        // Send handshake packet to client
        PacketOutHandShake00HandShake shake = new PacketOutHandShake00HandShake();
        handshakeResult = shake.getEquals();
        try {
            sendPacket(shake);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void run() {
        while (!closed) {
            try {
                String line = in.readLine();
                if (line == null)
                    break;

                JSONParser parser = new JSONParser();
                try {
                    JSONObject obj = (JSONObject) parser.parse(line);
                    if (!obj.containsKey("id") || !obj.containsKey("stage") || !obj.containsKey("args") || !(obj.get("id") instanceof Long) || PacketStage.fromString(obj.get("stage").toString()) == null)
                        throw new PacketException(obj, "Missing or invalid arguments");

                    handlePacket(new CRPacketIn(PacketStage.fromString(obj.get("stage").toString()), (long) obj.get("id"), (JSONObject) obj.get("args")));
                } catch (ParseException | PacketException ex) {
                    // In handshake stage. The client must first do the handshake, before we accept any other packets
                    if (!connected)
                        close();

                    if (ServerFactory.isShowingErrors())
                        System.err.println("Received: " + line);

                    // Message not formatted as json string. Ignore
                    System.err.print("Invalid packet received!");
                    if (ServerFactory.isDebug())
                        System.err.print(" (Server)");
                    System.err.println();

                    if (ServerFactory.isShowingErrors()) {
                        System.err.println("Received: " + line);
                        ex.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                break;
            }
        }
    }

    private void handlePacket(PacketIn packet) {
        if (server.doCustomHandshake()) {
            //region check in handshake stage
            if (packet.getStage() != PacketStage.HANDSHAKE && !connected) {
                // In handshake stage. The client must first do the handshake, before we accept any other packets
                if (ServerFactory.isShowingErrors())
                    System.err.println("Received a non-handshake packet in handshake stage!\nReceived packet #" + packet.getID() + " for stage " + packet.getStage());

                try {
                    close();
                } catch (IOException ex) {
                    // Already closed...
                }
                return;
            }
            //endregion
        }

        // Handle ping packet
        if (packet.getStage() == PacketStage.PING && packet.getID() == 0) {
            ping.refresh();
            return;
        }

        // Ignore if wrong stage
        if (server.doCustomHandshake() && ((packet.getStage() == PacketStage.HANDSHAKE && connected) || (packet.getStage() == PacketStage.CONNECTED && !connected)))
            return;

        if (server.doCustomHandshake()) {
            //region Handle handshake response
            if (packet.getStage() == PacketStage.HANDSHAKE && packet.getID() == 0) {
                PacketInHandShake00Response resp = new PacketInHandShake00Response(packet.getArgs());
                if (resp.getResult() == handshakeResult) {
                    connected = true;

                    if (ServerFactory.isDebug())
                        System.out.println("[DEBUG] Handshake completed for client " + id);

                    // Call connect event
                    ClientConnectEvent connEvt = new ClientConnectEvent(this);
                    server.getEventManager().callEvent(connEvt);

                    PacketOutHandshake01Response respOut = new PacketOutHandshake01Response(true, id.toString());

                    // Set the result to failed, if one of the events disallowed the connection
                    if (connEvt.isDisallowed())
                        respOut = new PacketOutHandshake01Response(false, connEvt.getReason());
                    try {
                        sendPacket(respOut);
                    } catch (IOException ex) {
                        // Connection closed..
                    }
                    return;
                }

                // Close connection when an invalid handshake response was sent
                try {
                    PacketOutHandshake01Response respOut = new PacketOutHandshake01Response(false, "Invalid handshake response");
                    sendPacket(respOut);

                    close();
                } catch (IOException ex) {
                    // Already closed..
                }
                return;
            }
            //endregion
        }

        //region Handle notification response
        if (packet.getStage() == PacketStage.CONNECTED && packet.getID() == -1) {
            PacketInConnectedXXNotification not = new PacketInConnectedXXNotification(packet.getArgs());

            // Call notification event
            ClientNotificationEvent notEvt = new ClientNotificationEvent(this, not.getNotification(), not.getResponse());
            server.getEventManager().callEvent(notEvt);
            return;
        }
        //endregion

        // If a packet was received that can't be handled by this method, pass it to the event methods
        PacketReceiveEvent packetEvt = new PacketReceiveEvent(this, packet);
        server.getEventManager().callEvent(packetEvt);
    }

    public UUID getID() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        if (!server.doCustomHandshake())
            throw new IllegalAccessError("Custom handshake is not enabled");

        this.connected = connected;
    }

    public void sendRaw(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    public void sendPacket(PacketOut packet) throws IOException {
        // Send json object to connection
        sendRaw(packet.serialize().toJSONString());
    }

    public void sendNotification(String notification) throws IOException {
        // Send notification packet
        PacketOutConnectedXXNotification not = new PacketOutConnectedXXNotification(notification);
        sendPacket(not);
    }

    public void close() throws IOException {
        if (closed)
            return;

        closed = true;
        connected = false;
        server.removeConnection(this);
        socket.close();

        if (ServerFactory.isDebug())
            System.out.println("[DEBUG] Closed client");
    }

    public void close(String reason) throws IOException {
        if (closed)
            return;

        // Send disconnect packet
        PacketOutAny00Disconnect disc = new PacketOutAny00Disconnect(reason);
        sendPacket(disc);

        close();
    }

    public int getPing() {
        return ping.getPing();
    }

    public ClientPing getClientPing() {
        return ping;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long time) {
        timeout = time;
    }

    public SocketAddress getAddress() {
        return socket.getRemoteSocketAddress();
    }

    public Server getServer() {
        return server;
    }
}
