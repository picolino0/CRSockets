package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 */
public interface RegisteredListener {
    void execute(EventBase base, Event event);

    EventPriority getPriority();

    EventListener getListener();

    EventBase getBase();
}
