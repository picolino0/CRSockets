package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 */
public interface EventManager {

    /**
     * Lets all EventListener listening to this type of event know that this event has been called
     *
     * @param event The event to call
     */
    void callEvent(Event event);

    /**
     * Registers an EventListener to listen to events
     *
     * @param listener The listener to register
     */
    void registerListener(EventListener listener);

    /**
     * Unregisters an EventListener, so it won't receive any events anymore
     *
     * @param listener The listener to unregister
     */
    void unregisterListener(EventListener listener);
}
