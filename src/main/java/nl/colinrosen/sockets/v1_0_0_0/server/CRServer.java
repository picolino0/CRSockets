package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.EventManager;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.server.events.CREventManager;
import nl.colinrosen.sockets.v1_0_0_0.server.events.CRHandlerList;

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
    private boolean running;
    private PingHeartbeat heartbeat;

    private final List<CRConnection> connections;

    public CRServer(int port) {
        this.port = port;
        eventmanager = new CREventManager(this);
        connections = new ArrayList<>();
    }

    public void start() throws IOException {
        // Ignore if the server has already started
        if (socket == null || socket.isBound() || !socket.isClosed())
            return;

        // Start server
        socket = new ServerSocket(port);
        Thread th = new Thread(this);
        th.start();
    }

    public void run() {
        running = true;

        // Start heartbeat
        heartbeat = new PingHeartbeat(this);

        while (running) {
            try {
                Socket sock = socket.accept();
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
            for (CRConnection con : connections)
                con.close("Server closed");

            connections.clear();
        }

        // Unregister eventlisteners
        CRHandlerList.unregisterAll(this);

        socket.close();
        socket = null;
    }

    public void broadcast(PacketOut packet) throws IOException {
        for (CRConnection con : connections)
            con.sendPacket(packet);
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

    void removeConnection(CRConnection conn) {
        synchronized (connections) {
            connections.remove(conn);
        }
    }
}
