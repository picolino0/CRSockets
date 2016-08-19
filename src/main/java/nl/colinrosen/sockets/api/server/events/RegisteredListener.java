package nl.colinrosen.sockets.api.server.events;

import nl.colinrosen.sockets.api.server.Server;

/**
 * @author Colin Rosen
 */
public interface RegisteredListener {
    void execute(Server server, Event event);

    EventPriority getPriority();

    EventListener getListener();

    Server getServer();
}
