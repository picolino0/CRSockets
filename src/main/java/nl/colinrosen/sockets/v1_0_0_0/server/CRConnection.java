package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public class CRConnection implements Connection {

    public UUID getID() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public void sendRaw(String message) {

    }

    public void close() {
        // Do close stuff...
    }

    public void close(String reason) {

    }

    public int getPing() {
        return 0;
    }

    public long getTimeout() {
        return 0;
    }

    public void setTimeout(long time) {

    }

    public InetSocketAddress getAddress() {
        return null;
    }

    public Server getServer() {
        return null;
    }
}
