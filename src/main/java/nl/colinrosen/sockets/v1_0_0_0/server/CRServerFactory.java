package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;

/**
 * @author Colin Rosen
 */
public class CRServerFactory extends ServerFactory {
    public Server newServer(int port) {
        CRServer srv = new CRServer(port);
        addServer(srv);
        return srv;
    }

    public Server newServer(int port, boolean customHandshake){
        CRServer srv = new CRServer(port, customHandshake);
        addServer(srv);
        return srv;
    }
}
