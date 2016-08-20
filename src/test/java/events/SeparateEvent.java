package events;

import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;

/**
 * @author Colin Rosen
 */
public class SeparateEvent extends Event {
    private static HandlerList handlerlist;

    private String message;
    private int callCount = 0;

    public SeparateEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCallCount(){
        return callCount;
    }

    public void call(){
        callCount++;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
