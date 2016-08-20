package events;

import events.AnotherEvent;
import events.SeparateEvent;
import events.SomeEvent;
import nl.colinrosen.sockets.api.shared.events.EventHandler;
import nl.colinrosen.sockets.api.shared.events.EventListener;
import nl.colinrosen.sockets.api.shared.events.EventPriority;
import nl.colinrosen.sockets.api.shared.events.IllegalEventListenerException;

/**
 * @author Colin Rosen
 */
public class EventHandleTest implements EventListener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void handlePublic(SomeEvent evt) {
        System.out.println("[1]" + evt.getMessage());

        // Check order
        if (evt.getCalled() != 0)
            throw new IllegalEventListenerException("Wrong order");

        evt.incrementCalled();
    }

    @EventHandler(priority = EventPriority.LOW)
    private void handlePrivate(SomeEvent evt) {
        System.out.println("[2]" + evt.getMessage());

        // Check order
        if (evt.getCalled() != 1)
            throw new IllegalEventListenerException("Wrong order");

        evt.incrementCalled();
    }

    @EventHandler(priority = EventPriority.HIGH)
    void handlePackage(SomeEvent evt) {
        System.out.println("[4]" + evt.getMessage());

        // Check order
        if (evt.getCalled() != 3)
            throw new IllegalEventListenerException("Wrong order");

        evt.incrementCalled();
    }

    @EventHandler
    private void handleSecondEvent(AnotherEvent evt) {
        System.out.println("SECOND EVENT: " + evt.getMessage() + " - " + evt.getNr());

        evt.setCalled();
    }

    @EventHandler
    private void handleSeparateEvent(SeparateEvent evt){
        evt.call();
        System.out.println(evt.getMessage());
    }
}
