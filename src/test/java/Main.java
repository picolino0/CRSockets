import events.AnotherEvent;
import events.SomeEvent;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;
import nl.colinrosen.sockets.api.server.events.EventManager;
import nl.colinrosen.sockets.api.server.events.IllegalEventListenerException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Colin Rosen
 */
public class Main {
    Server serv = ServerFactory.newServerInstance();

    @Test
    public void doEventTest() {
        EventManager manager = serv.getEventManager();

        // Register listeners
        manager.registerListener(new EventTest());
        manager.registerListener(new EventTestSecondary());

        // Call first event and check if it is executed in the right order
        manager.callEvent(new SomeEvent("First event"));
        System.out.println();

        // Call second event
        AnotherEvent evt = new AnotherEvent("Second event", 42);
        manager.callEvent(evt);

        // Check if the additional method was called in addition to the other methods (because AnotherEvent is a subclass of SomeEvent)
        Assert.assertEquals(true, evt.wasCalled());
        Assert.assertEquals(5, evt.getCalled());
    }
}
