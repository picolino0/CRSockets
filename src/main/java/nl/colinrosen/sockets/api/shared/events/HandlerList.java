package nl.colinrosen.sockets.api.shared.events;

import java.util.Collection;

/**
 * @author Colin Rosen
 */
public interface HandlerList {
    /**
     * Registers an EventListener to be called when the event associated with this handlerlist is called
     *
     * @param listener The listener to add
     */
    void register(RegisteredListener listener);

    /**
     * Adds all listeners in the collection to the HandlerList
     *
     * @param listeners The listeners to add
     */
    void registerAll(Collection<RegisteredListener> listeners);

    /**
     * Unregisters an EventListener so it won't be called anymore when an event is called
     *
     * @param listener The listener to remove
     */
    void unregister(RegisteredListener listener);

    /**
     * Unregisters an EventListener so it won't be called anymore when an event is called
     *
     * @param listener The listener to remove
     */
    void unregister(EventListener listener);

    /**
     * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
     */
    void bake();

    /**
     * @return The listeners in this HandlerList
     */
    RegisteredListener[] getHandlers();
}
