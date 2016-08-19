package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.api.server.events.EventManager;

/**
 * @author Colin Rosen
 */
public interface Server {

    void start();

    void close();

    void broadcast(nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut packet);

    EventManager getEventManager();
}
