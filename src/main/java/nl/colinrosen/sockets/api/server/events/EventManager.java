package nl.colinrosen.sockets.api.server.events;

/**
 * @author Colin Rosen
 */
public interface EventManager {

    void callEvent(Event event);

    void registerListener(EventListener listener);

    void unregisterListener(EventListener listener);

    HandlerList newHandlerList();
}
