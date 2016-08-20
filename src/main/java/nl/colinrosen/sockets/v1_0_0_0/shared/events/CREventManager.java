package nl.colinrosen.sockets.v1_0_0_0.shared.events;

import nl.colinrosen.sockets.api.shared.events.*;
import nl.colinrosen.sockets.api.shared.events.EventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author Colin Rosen
 */
public class CREventManager implements EventManager {

    private EventBase base;

    public CREventManager(EventBase base) {
        this.base = base;
    }

    public void callEvent(Event event) {
        HandlerList handlers = event.getHandlers();

        // If handlers is not set, we can be sure that no listeners have been set
        // (as it sets the value to a new handlerlist everytime a listener is registered and this variable is null)
        // Or someone has manually set this variable to null. in which case, also ignore the event..
        if (handlers == null)
            return;

        for (RegisteredListener list : handlers.getHandlers())
            list.execute(base, event);
    }

    public void registerListener(EventListener listener) {
        for (Map.Entry<Class<? extends Event>, List<RegisteredListener>> entry : createRegisteredListeners(listener).entrySet()) {
            HandlerList handlers = getHandlerList(entry.getKey());
            handlers.registerAll(entry.getValue());
        }
    }

    private HandlerList getHandlerList(Class<? extends Event> clazz) {
        try {
            Field f = clazz.getDeclaredField("handlerlist");
            if (!Modifier.isStatic(f.getModifiers()))
                throw new NoSuchFieldError("Field was not static");

            f.setAccessible(true);
            if (f.get(null) == null)
                f.set(null, newHandlerList());
            return (HandlerList) f.get(null);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass()))
                return getHandlerList(clazz.getSuperclass().asSubclass(Event.class));

            throw new IllegalEventListenerException("The private static 'handlerlist' variable could not be found in the eventlistener or it's super classes");
        } catch (IllegalAccessException ex) {
            throw new IllegalEventListenerException(ex.getMessage());
        }
    }

    private Map<Class<? extends Event>, List<RegisteredListener>> createRegisteredListeners(EventListener listener) {
        Map<Class<? extends Event>, List<RegisteredListener>> registeredListeners = new HashMap<>();
        Set<Method> methods; // Use a set to get rid of duplicate methods

        // Get all methods, including private methods, in one hashset
        Method[] publicMethods = listener.getClass().getMethods();
        Method[] privateMethods = listener.getClass().getDeclaredMethods();
        methods = new HashSet<>(publicMethods.length + privateMethods.length, 1);

        for (Method m : publicMethods)
            methods.add(m);
        for (Method m : privateMethods)
            methods.add(m);

        // Loop through all methods to check if they're eventhandlers and deal with them
        for (Method method : methods) {
            if (method.isBridge() || method.isSynthetic())
                continue; // Ignore bridge and synthetic methods to prevent duplication

            EventHandler handler = method.getAnnotation(EventHandler.class);
            if (handler == null)
                continue; // Method is not an event handler

            // Check parameters
            if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0]))
                throw new IllegalEventListenerException("An invalid EventHandler was found in the listener: " + method.toGenericString() + " in " + listener.getClass());

            // Get event type
            Class<? extends Event> eventType = method.getParameterTypes()[0].asSubclass(Event.class);

            method.setAccessible(true); // Make method accessible so it can be invoked

            // Get registered listeners list for this event type or create a new one if it doesn't exist.
            // This allows a listener to have multiple methods for a single event type
            List<RegisteredListener> eventListeners = registeredListeners.get(eventType);
            if (eventListeners == null) {
                eventListeners = new ArrayList<>();
                registeredListeners.put(eventType, eventListeners);
            }

            // Add registered listener to list
            eventListeners.add(new CRRegisteredListener(handler.priority(), listener, method, eventType, base));
        }

        return registeredListeners;
    }

    public void unregisterListener(EventListener listener) {
        CRHandlerList.unregisterAll(listener);
    }

    public HandlerList newHandlerList() {
        return new CRHandlerList();
    }
}
