package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.v1_0_0_0.server.CRServerFactory;

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

    public static ServerFactory getInstance() {
        if (instance == null)
            new CRServerFactory();

        return instance;
    }

    public static void closeServers() {
        servers.forEach(Server::close);
    }

    public static Server newServerInstance() {
        return getInstance().newServer();
    }

    public abstract Server newServer();
}
