package nl.colinrosen.sockets.api.server.events;

/**
 * @author Colin Rosen
 */
public abstract class Event {

    public abstract HandlerList getHandlers();
}
