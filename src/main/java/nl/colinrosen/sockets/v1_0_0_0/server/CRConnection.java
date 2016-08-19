package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.client.ClientConnectEvent;
import nl.colinrosen.sockets.api.server.events.client.ClientNotificationEvent;
import nl.colinrosen.sockets.api.server.events.packets.PacketReceiveEvent;
import nl.colinrosen.sockets.api.server.packets.PacketException;
import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketEnum;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.api.server.packets.outgoing.TransientField;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming.CRPacketIn;
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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashSet;
import java.util.Set;
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

        try {
            close();
        } catch (IOException ex) {
            // Already closed
        }
    }

    private void handlePacket(PacketIn packet) {
        // Handle ping packet
        if (packet.getStage() == PacketStage.PING && packet.getID() == 0) {
            ping.refresh();
            return;
        }

        // Ignore if wrong stage
        if ((packet.getStage() == PacketStage.HANDSHAKE && connected) || (packet.getStage() == PacketStage.CONNECTED && !connected))
            return;

        //region Handle handshake response
        if (packet.getStage() == PacketStage.HANDSHAKE && packet.getID() == 0) {
            PacketInHandShake00Response resp = new PacketInHandShake00Response((JSONObject) packet.getArgs().get("args"));
            if (resp.getResult() == handshakeResult) {
                connected = true;

                // Call connect event
                ClientConnectEvent connEvt = new ClientConnectEvent(this);
                server.getEventManager().callEvent(connEvt);

                PacketOutHandshake01Response respOut = new PacketOutHandshake01Response(true, "Handshake completed");

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

        //region Handle notification response
        if (packet.getStage() == PacketStage.CONNECTED && packet.getID() == -1) {
            PacketInConnectedXXNotification not = new PacketInConnectedXXNotification((JSONObject) packet.getArgs().get("args"));

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

    public void sendRaw(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    public void sendPacket(PacketOut packet) throws IOException {
        // Get all fields in the packet
        Field[] publicFields = packet.getClass().getFields();
        Field[] privateFields = packet.getClass().getDeclaredFields();

        // Add all fields to a single hashset
        Set<Field> fields = new HashSet<>(publicFields.length + privateFields.length, 1);
        for (Field f : publicFields)
            fields.add(f);
        for (Field f : privateFields)
            fields.add(f);

        // Create json object for packet
        JSONObject obj = new JSONObject();
        obj.put("stage", packet.getStage().name());
        obj.put("id", packet.getID());

        // Compile fields in a json object
        JSONObject args = new JSONObject();
        for (Field f : fields) {
            TransientField trans = f.getAnnotation(TransientField.class);
            if (trans != null || f.isSynthetic())
                // Ignore field if it is marked transient or if the field is synthetic
                continue;

            try {
                f.setAccessible(true);
                Object value = f.get(packet);

                // Determine how to serialize an enum field
                if (value != null && Enum.class.isAssignableFrom(f.getType())) {
                    PacketEnum.ValueType type = PacketEnum.ValueType.NAME;
                    String method = "";

                    PacketEnum pe = f.getAnnotation(PacketEnum.class);
                    if (pe != null) {
                        type = pe.type();
                        method = pe.method();
                    }

                    if (type == PacketEnum.ValueType.NAME)
                        value = ((Enum) value).name();
                    if (type == PacketEnum.ValueType.ORDINAL)
                        value = ((Enum) value).ordinal();
                    if (type == PacketEnum.ValueType.METHOD_VAL) {
                        try {
                            value = value.getClass().getMethod(method).invoke(value);
                        } catch (NoSuchMethodException | InvocationTargetException ex) {
                            // Reset value if method didn't exist
                            value = f.get(packet);
                        }
                    }
                }

                args.put(f.getName(), value);
            } catch (IllegalAccessException ex) {
                // Ignore
            }
        }

        obj.put("args", args);

        // Send json object to connection
        sendRaw(obj.toJSONString());
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
        server.removeConnection(this);
        socket.close();
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
