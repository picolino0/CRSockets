package nl.colinrosen.sockets.api.server.events;

import java.util.Collection;

/**
 * @author Colin Rosen
 */
public interface HandlerList {
    void register(RegisteredListener listener);

    void registerAll(Collection<RegisteredListener> listeners);

    void unregister(RegisteredListener listener);

    void unregister(EventListener listener);

    void bake();

    RegisteredListener[] getHandlers();
}
