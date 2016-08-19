package nl.colinrosen.sockets.api.server.events.client;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.events.Event;
import nl.colinrosen.sockets.api.server.events.HandlerList;

/**
 * @author Colin Rosen
 *         <p>
 *         Base class for all events for server clients
 *         </p>
 */
public abstract class ClientEvent extends Event {
    private static HandlerList handlerlist = null;

    private Connection client;

    public ClientEvent(Connection client) {
        this.client = client;
    }

    public Connection getClient() {
        return client;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
