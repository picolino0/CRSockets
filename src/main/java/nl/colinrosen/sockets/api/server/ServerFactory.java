package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.v1_0_0_0.server.CRServerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Colin Rosen
 */
public abstract class ServerFactory {
    private static ServerFactory instance;
    private static List<Server> servers = new ArrayList<>();

    protected ServerFactory() {
        instance = this;
    }

    /**
     * @return The singleton instance of the ServerFactory or creates a new one when there isn't an instance
     */
    public static ServerFactory getInstance() {
        if (instance == null)
            new CRServerFactory();

        return instance;
    }

    /**
     * Close all servers that have been created by this factory
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public static void closeServers() throws IOException {
        for (Server serv : servers)
            serv.close();
    }

    /**
     * Alias to {@link #newServer(int)}
     *
     * @param port The port the new server should connect to
     */
    public static Server newServerInstance(int port) {
        return getInstance().newServer(port);
    }

    /**
     * Creates a new instance of Server with the given port
     * <p>
     * This does not start the server yet.
     * So if you get a server instance on the same port twice,
     * the I/O Exception will be thrown once the start() method has
     * been called on the Server instances
     *
     * @param port The port the server needs to bind to
     */
    public abstract Server newServer(int port);
}
