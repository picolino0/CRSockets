package nl.colinrosen.sockets.api.client.events;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;

import java.util.UUID;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when the handshake was successfully completed
 */
public class ConnectionEvent extends Event {

    private static HandlerList handlerlist;

    private UUID uuid;

    public ConnectionEvent(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
