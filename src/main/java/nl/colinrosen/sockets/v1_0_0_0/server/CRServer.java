package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.EventManager;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.v1_0_0_0.server.events.CREventManager;

/**
 * @author Colin Rosen
 */
public class CRServer implements Server {

    private CREventManager eventmanager;

    public CRServer() {
        eventmanager = new CREventManager(this);
    }

    public void start() {

    }

    public void close() {

    }

    public void broadcast(PacketOut packetOut) {

    }

    public EventManager getEventManager() {
        return eventmanager;
    }
}
