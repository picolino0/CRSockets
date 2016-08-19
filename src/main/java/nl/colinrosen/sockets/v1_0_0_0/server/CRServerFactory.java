package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;

/**
 * @author Colin Rosen
 */
public class CRServerFactory extends ServerFactory {
    public Server newServer() {
        return new CRServer();
    }
}
