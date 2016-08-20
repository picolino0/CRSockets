package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;
import nl.colinrosen.sockets.api.shared.events.EventManager;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutAny00Disconnect;
import nl.colinrosen.sockets.v1_0_0_0.shared.events.CREventManager;
import nl.colinrosen.sockets.v1_0_0_0.shared.events.CRHandlerList;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public class CRServer implements Server, Runnable {

    private CREventManager eventmanager;
    private int port;
    private ServerSocket socket;
    private boolean customHandshake, running;
    private PingHeartbeat heartbeat;

    private final List<CRConnection> connections;

    CRServer(int port) {
        this.port = port;
        eventmanager = new CREventManager(this);
        connections = new ArrayList<>();
    }

    CRServer(int port, boolean customHandshake) {
        this(port);

        this.customHandshake = customHandshake;
    }

    public void start() throws IOException {
        // Ignore if the server has already started
        if (running)
            return;

        if (ServerFactory.isDebug())
            System.out.println("[DEBUG] Starting server...");

        // Start server
        socket = new ServerSocket(port);
        new Thread(this).start();
    }

    public void run() {
        running = true;

        if (ServerFactory.isDebug())
            System.out.println("[DEBUG] Server is running");

        // Start heartbeat
        heartbeat = new PingHeartbeat(this);

        while (running) {
            try {
                Socket sock = socket.accept();

                if (ServerFactory.isDebug())
                    System.out.println("[DEBUG] Client connected (" + sock.getRemoteSocketAddress() + ")");

                connections.add(new CRConnection(this, sock));
            } catch (IOException ex) {
                break;
            }
        }
    }

    public void close() throws IOException {
        // Ignore if the server hasn't started yet
        if (!running)
            return;

        running = false;

        // Close connections
        synchronized (connections) {
            if (ServerFactory.isDebug() && connections.size() > 0)
                System.out.println("[DEBUG] Closing clients");

            List<CRConnection> temp = new ArrayList(connections);
            for (CRConnection con : temp)
                con.close("Server closed");

            connections.clear();
        }

        // Unregister eventlisteners
        CRHandlerList.unregisterAll(this);

        socket.close();
        socket = null;

        if (ServerFactory.isDebug())
            System.out.println("[DEBUG] Server closed");
    }

    public void close(String reason) throws IOException {
        // Ignore if the server hasn't started yet
        if (!running)
            return;

        // Close connections with given reason
        synchronized (connections) {
            if (ServerFactory.isDebug())
                System.out.println("[DEBUG] Closing clients");

            List<CRConnection> temp = new ArrayList(connections);
            for (CRConnection con : temp)
                con.close(reason);
        }

        // Close the server
        close();
    }

    public void broadcast(PacketOut packet) throws IOException {
        for (CRConnection con : connections)
            con.sendPacket(packet);
    }

    public void broadcastNotification(String notification) throws IOException {
        for (CRConnection conn : connections)
            conn.sendNotification(notification);
    }

    public EventManager getEventManager() {
        return eventmanager;
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public Connection getConnection(UUID uuid) {
        for (CRConnection con : connections)
            if (con.getID().equals(uuid))
                return con;

        return null;
    }

    public List<Connection> getConnections() {
        return new ArrayList<>(connections);
    }

    public boolean doCustomHandshake(){
        return customHandshake;
    }

    void removeConnection(CRConnection conn) {
        synchronized (connections) {
            connections.remove(conn);
        }
    }
}
