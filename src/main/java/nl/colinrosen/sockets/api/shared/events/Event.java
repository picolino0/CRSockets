package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 *         <p>
 *         The base class for all events
 *         </p>
 */
public abstract class Event {

    /**
     * The handlerlist is used to determine which eventlisteners
     * should be notified with this event
     * <p>
     * The EventManager will automatically set the handlerlist variable
     *
     * @return The static handlerlist variable that all events should have
     */
    public abstract HandlerList getHandlers();
}
