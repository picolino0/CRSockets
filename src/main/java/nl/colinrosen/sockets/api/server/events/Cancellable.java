package nl.colinrosen.sockets.api.server.events;

/**
 * @author Colin Rosen
 */
public interface Cancellable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);
}
