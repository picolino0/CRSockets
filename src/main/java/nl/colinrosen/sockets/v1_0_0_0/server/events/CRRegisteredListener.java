package nl.colinrosen.sockets.v1_0_0_0.server.events;


import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.Event;
import nl.colinrosen.sockets.api.server.events.EventListener;
import nl.colinrosen.sockets.api.server.events.EventPriority;
import nl.colinrosen.sockets.api.server.events.RegisteredListener;

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
    private Server server;

    CRRegisteredListener(EventPriority priority, EventListener listener, Method method, Class<? extends Event> clazz, Server server) {
        this.priority = priority;
        this.listener = listener;
        this.method = method;
        this.clazz = clazz;
        this.server = server;
    }

    public void execute(Server server, Event event) {
        // Wrong event type or wrong server
        if (!clazz.isAssignableFrom(event.getClass()) || !server.equals(this.server))
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

    public Server getServer(){
        return server;
    }
}
