package events;

import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;

/**
 * @author Colin Rosen
 */
public class SomeEvent extends Event {

    private static HandlerList handlerlist = null;
    private String message;

    private int called;

    public SomeEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCalled() {
        return called;
    }

    public void incrementCalled() {
        called++;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
