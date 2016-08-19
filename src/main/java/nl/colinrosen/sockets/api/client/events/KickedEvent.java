package nl.colinrosen.sockets.api.client.events;

import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when the client is kicked from the server
 */
public class KickedEvent extends Event {

    private static HandlerList handlerlist;

    private String reason;

    public KickedEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
