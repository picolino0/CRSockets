package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.EventManager;
import nl.colinrosen.sockets.api.server.events.HandlerList;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.server.events.CREventManager;
import nl.colinrosen.sockets.v1_0_0_0.server.events.CRHandlerList;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Colin Rosen
 */
public class CRServer implements Server {

    private CREventManager eventmanager;
    private int port;
    private ServerSocket socket;

    private List<CRConnection> connections;

    public CRServer(int port) {
        this.port = port;
        eventmanager = new CREventManager(this);
        connections = new ArrayList<>();
    }

    public void start() throws IOException {
        // Ignore if the server has already started
        if (socket == null || socket.isBound() || !socket.isClosed())
            return;

        socket = new ServerSocket(port);
    }

    public void close() throws IOException {
        // Ignore if the server hasn't started yet
        if (socket == null || socket.isClosed())
            return;

        // Close connections
        connections.forEach((conn) -> conn.close("Server closed"));
        connections.clear();

        // Unregister eventlisteners
        CRHandlerList.unregisterAll(this);

        socket.close();
    }

    public void broadcast(PacketOut packetOut) {

    }

    public EventManager getEventManager() {
        return eventmanager;
    }

    public int getPort() {
        return port;
    }
}
