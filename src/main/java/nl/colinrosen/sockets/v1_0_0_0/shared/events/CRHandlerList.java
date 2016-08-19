package nl.colinrosen.sockets.v1_0_0_0.shared.events;

import nl.colinrosen.sockets.api.shared.events.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * @author Colin Rosen
 */
public class CRHandlerList implements HandlerList {
    private CRRegisteredListener[] handlers;

    private final EnumMap<EventPriority, List<RegisteredListener>> handlerSlots;

    private static final List<HandlerList> allLists = new ArrayList<>();

    public CRHandlerList() {
        handlerSlots = new EnumMap<>(EventPriority.class);
        for (EventPriority ep : EventPriority.values())
            handlerSlots.put(ep, new ArrayList<>());

        synchronized (allLists) {
            allLists.add(this);
        }
    }

    public synchronized void register(RegisteredListener listener) {
        if (handlerSlots.get(listener.getPriority()).contains(listener))
            return;

        handlers = null;
        handlerSlots.get(listener.getPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        listeners.forEach(this::register);
    }

    public synchronized void unregister(RegisteredListener listener) {
        if (handlerSlots.get(listener.getPriority()).remove(listener))
            handlers = null;
    }

    public synchronized void unregister(EventListener listener) {
        for (List<RegisteredListener> listeners : handlerSlots.values()) {
            List<RegisteredListener> temp = new ArrayList(listeners);
            for (RegisteredListener regList : temp)
                if (regList.getListener().equals(listener)) {
                    listeners.remove(regList);
                    handlers = null;
                }
        }
    }

    public void bake() {
        if (handlers != null)
            return; // No need to bake again, list hasn't changed

        // Get a single list of all handlers, ordered by priority
        List<RegisteredListener> handlers = new ArrayList<>();
        handlerSlots.forEach((priority, registeredListeners) -> handlers.addAll(registeredListeners));
        this.handlers = handlers.toArray(new CRRegisteredListener[handlers.size()]);
    }

    public CRRegisteredListener[] getHandlers() {
        if (handlers == null)
            bake();

        return handlers.clone();
    }

    public static void unregisterAll() {
        synchronized (allLists) {
            for (HandlerList hlist : allLists)
                synchronized (hlist) {
                    CRHandlerList list = (CRHandlerList) hlist;
                    list.handlerSlots.values().forEach(List::clear);
                    list.handlers = null;
                }
        }
    }

    public static void unregisterAll(EventBase base) {
        synchronized (allLists) {
            for (HandlerList list : allLists) {
                RegisteredListener[] listeners = list.getHandlers();
                for (RegisteredListener listener : listeners) {
                    if (listener.getBase().equals(base))
                        list.unregister(listener);
                }
            }
        }
    }

    public static void unregisterAll(RegisteredListener listener) {
        synchronized (allLists) {
            allLists.forEach((hlist) -> hlist.unregister(listener));
        }
    }

    public static void unregisterAll(EventListener listener) {
        synchronized (allLists) {
            allLists.forEach((hlist) -> hlist.unregister(listener));
        }
    }

    public static void bakeAll() {
        synchronized (allLists) {
            allLists.forEach(HandlerList::bake);
        }
    }

    public static List<HandlerList> getHandlerLists() {
        synchronized (allLists) {
            return new ArrayList<>(allLists);
        }
    }
}
