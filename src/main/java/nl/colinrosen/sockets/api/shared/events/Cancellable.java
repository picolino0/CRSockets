package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 */
public interface Cancellable {

    /**
     * @return If the event was cancelled
     */
    boolean isCancelled();

    /**
     * Cancels the event. The event will still continue on to the next EventListeners
     * Which may set the cancelled state to false again
     *
     * @param cancelled
     */
    void setCancelled(boolean cancelled);
}
