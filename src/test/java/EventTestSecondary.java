import events.SomeEvent;
import nl.colinrosen.sockets.api.shared.events.EventHandler;
import nl.colinrosen.sockets.api.shared.events.EventListener;
import nl.colinrosen.sockets.api.shared.events.EventPriority;
import nl.colinrosen.sockets.api.shared.events.IllegalEventListenerException;

/**
 * @author Colin Rosen
 */
public class EventTestSecondary implements EventListener {

    @EventHandler
    private void handle(SomeEvent evt) {
        System.out.println("[3]" + evt.getMessage());

        // Check order
        if (evt.getCalled() != 2)
            throw new IllegalEventListenerException("Wrong order");

        evt.incrementCalled();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void handleLast(SomeEvent evt) {
        System.out.println("[5]" + evt.getMessage());

        // Check order
        if (evt.getCalled() != 4)
            throw new IllegalEventListenerException("Wrong order");

        evt.incrementCalled();
    }
}
