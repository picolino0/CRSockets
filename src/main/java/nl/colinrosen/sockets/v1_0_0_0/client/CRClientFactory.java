package nl.colinrosen.sockets.v1_0_0_0.client;

import nl.colinrosen.sockets.api.client.Client;
import nl.colinrosen.sockets.api.client.ClientFactory;

import java.net.UnknownHostException;

/**
 * @author Colin Rosen
 */
public class CRClientFactory extends ClientFactory {

    public Client newClient(String address, int port) throws UnknownHostException {
        CRClient cl = new CRClient(address, port);
        addClient(cl);
        return cl;
    }
}
