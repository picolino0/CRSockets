import events.AnotherEvent;
import events.SomeEvent;
import nl.colinrosen.sockets.api.server.events.EventHandler;
import nl.colinrosen.sockets.api.server.events.EventListener;
import nl.colinrosen.sockets.api.server.events.EventPriority;
import nl.colinrosen.sockets.api.server.events.IllegalEventListenerException;
import org.junit.Assert;

/**
 * @author Colin Rosen
 */
public class EventTest implements EventListener {
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
}