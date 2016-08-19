package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 */
public interface EventBase {
    /**
     * Gets the EventManager associated with this server
     */
    EventManager getEventManager();
}
