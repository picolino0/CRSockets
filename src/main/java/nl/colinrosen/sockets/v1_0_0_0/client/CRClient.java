package nl.colinrosen.sockets.v1_0_0_0.client;

import nl.colinrosen.sockets.api.client.Client;
import nl.colinrosen.sockets.api.client.events.ConnectionEvent;
import nl.colinrosen.sockets.api.client.events.KickedEvent;
import nl.colinrosen.sockets.api.client.events.NotificationEvent;
import nl.colinrosen.sockets.api.client.events.PacketReceiveEvent;
import nl.colinrosen.sockets.api.shared.events.EventManager;
import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming.*;
import nl.colinrosen.sockets.v1_0_0_0.client.packets.outgoing.PacketOutConnectedXXNotification;
import nl.colinrosen.sockets.v1_0_0_0.client.packets.outgoing.PacketOutHandshake00Response;
import nl.colinrosen.sockets.v1_0_0_0.shared.events.CREventManager;
import nl.colinrosen.sockets.v1_0_0_0.shared.events.CRHandlerList;
import nl.colinrosen.sockets.v1_0_0_0.shared.packets.CRPacketIn;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public class CRClient implements Client, Runnable {
    private UUID id;
    private int ping;
    private boolean connected, running;
    private CREventManager eventManager;
    private InetAddress address;
    private int port;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    CRClient(String address, int port) throws UnknownHostException {
        this.address = InetAddress.getByName(address);
        this.port = port;
        eventManager = new CREventManager(this);

    }

    public void start() throws IOException {
        if (running)
            return;

        socket = new Socket(address, port);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Start listening thread
        new Thread(this).start();
    }

    public void run() {
        running = true;

        while (running) {
            try {
                String line = in.readLine();
                if (line == null)
                    break;

                JSONParser parser = new JSONParser();
                try {
                    JSONObject obj = (JSONObject) parser.parse(line);
                    if (!obj.containsKey("id") || !obj.containsKey("stage") || !obj.containsKey("args") || !(obj.get("id") instanceof Integer) || PacketStage.fromString(obj.get("stage").toString()) == null)
                        throw new PacketException(obj, "Missing or invalid arguments");

                    handlePacket(new CRPacketIn(PacketStage.fromString(obj.get("stage").toString()), (int) obj.get("id"), (JSONObject) obj.get("args")));
                } catch (ParseException | PacketException ex) {
                    // Message not formatted as json string. Ignore
                    System.err.println("Invalid packet received!");
                }
            } catch (IOException ex) {
                break;
            }
        }
    }

    private void handlePacket(PacketIn packet) {
        // Handle ping packet
        if (packet.getStage() == PacketStage.PING && packet.getID() == 0) {
            PacketInPing00Ping pingPack = new PacketInPing00Ping(packet.getArgs());
            ping = pingPack.getPing();
            return;
        }

        // Ignore if wrong stage
        if ((packet.getStage() == PacketStage.HANDSHAKE && connected) || (packet.getStage() == PacketStage.CONNECTED && !connected))
            return;

        //region Handle kick packet
        if (packet.getStage() == PacketStage.ANY && packet.getID() == 0) {
            PacketInAny00Disconnect discon = new PacketInAny00Disconnect(packet.getArgs());

            KickedEvent kickEvt = new KickedEvent(discon.getReason());
            eventManager.callEvent(kickEvt);
            return;
        }
        //endregion

        //region Handle handshake
        if (packet.getStage() == PacketStage.HANDSHAKE && packet.getID() == 0) {
            PacketInHandshake00Handshake shakePack = new PacketInHandshake00Handshake(packet.getArgs());

            // Send result calculated in the PacketInHandshake00Handshake constructor
            PacketOutHandshake00Response shakeRes = new PacketOutHandshake00Response(shakePack.getResult());
            try {
                sendPacket(shakeRes);
            } catch (IOException ex) {
                // Client closed...
            }

            return;
        }

        if (packet.getStage() == PacketStage.HANDSHAKE && packet.getID() == 1) {
            PacketInHandshake01Response shakeRespPack = new PacketInHandshake01Response(packet.getArgs());

            // Client was disallowed, or the handshake somehow failed
            if (!shakeRespPack.isSuccess()) {
                KickedEvent kickEvt = new KickedEvent(shakeRespPack.getMessage());
                eventManager.callEvent(kickEvt);
                return;
            }

            id = UUID.fromString(shakeRespPack.getMessage());
            connected = true;

            // Call connection event
            ConnectionEvent evt = new ConnectionEvent(id);
            eventManager.callEvent(evt);
            return;
        }
        //endregion

        //region Handle notification packet
        if (packet.getStage() == PacketStage.CONNECTED && packet.getID() == -1) {
            PacketInConnectedXXNotification notPack = new PacketInConnectedXXNotification(packet.getArgs());

            NotificationEvent notEvt = new NotificationEvent(notPack.getNotification());
            eventManager.callEvent(notEvt);

            // send response, if there is one
            if (notEvt.getResponse() != null) {
                PacketOutConnectedXXNotification notPackOut = new PacketOutConnectedXXNotification(notPack.getNotification(), notEvt.getResponse());
                try {
                    sendPacket(notPackOut);
                } catch (IOException ex) {
                    // Client closed...
                }
            }
            return;
        }
        //endregion

        // If a packet was received that can't be handled by this method, pass it to the event methods
        PacketReceiveEvent packEvt = new PacketReceiveEvent(packet);
        eventManager.callEvent(packEvt);
    }

    public void close() throws IOException {
        if (!running)
            return;

        running = false;
        connected = false;

        // Unregister events
        CRHandlerList.unregisterAll(this);

        socket.close();
        socket = null;
    }

    public void sendRaw(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    public void sendPacket(PacketOut packet) throws IOException {
        // Send json object to server
        sendRaw(packet.serialize().toJSONString());
    }

    public UUID getID() {
        return id;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public SocketAddress getAddress() {
        return socket.getRemoteSocketAddress();
    }

    public boolean isConnected() {
        return connected;
    }

    public int getPing() {
        return ping;
    }
}
