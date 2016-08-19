package nl.colinrosen.sockets.v1_0_0_0.shared.events;


import nl.colinrosen.sockets.api.shared.events.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Colin Rosen
 */
public class CRRegisteredListener implements RegisteredListener {
    private EventPriority priority;
    private EventListener listener;
    private Method method;
    private Class<? extends Event> clazz;
    private EventBase base;

    CRRegisteredListener(EventPriority priority, EventListener listener, Method method, Class<? extends Event> clazz, EventBase base) {
        this.priority = priority;
        this.listener = listener;
        this.method = method;
        this.clazz = clazz;
        this.base = base;
    }

    public void execute(EventBase base, Event event) {
        // Wrong event type or wrong server
        if (!clazz.isAssignableFrom(event.getClass()) || !this.base.equals(base))
            return;

        // Invoke event
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    public EventPriority getPriority() {
        return priority;
    }

    public EventListener getListener() {
        return listener;
    }

    public EventBase getBase() {
        return base;
    }
}
