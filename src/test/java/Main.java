import events.AnotherEvent;
import events.SomeEvent;
import nl.colinrosen.sockets.api.client.Client;
import nl.colinrosen.sockets.api.client.ClientFactory;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.ServerFactory;
import nl.colinrosen.sockets.api.shared.events.EventManager;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.junit.Assert.fail;

/**
 * @author Colin Rosen
 */
public class Main {
    private static Server serv;
    private static Client client;

    @BeforeClass
    public static void prepare() {
        serv = ServerFactory.newServerInstance(2585);

        try {
            client = ClientFactory.newClientInstance("localhost", 2585);
        } catch (UnknownHostException ex) {
            fail("Host not found for client");
        }
    }

    @AfterClass
    public static void cleanUp() {
        try {
            ServerFactory.closeServers();
            ClientFactory.closeClients();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void doEventTest() {
        EventManager manager = serv.getEventManager();
        EventManager manager2 = client.getEventManager();

        // Register listeners
        manager.registerListener(new EventTest());
        manager.registerListener(new EventTestSecondary());

        // Register listener for client event manager too
        manager2.registerListener(new EventTest());

        // Call first event and check if it is executed in the right order
        manager.callEvent(new SomeEvent("First event"));
        System.out.println();

        // Call second event
        AnotherEvent evt = new AnotherEvent("Second event", 42);
        manager.callEvent(evt);
        System.out.println();
        System.out.println();

        // Check if the new method was called in addition to the other methods (because AnotherEvent is a subclass of SomeEvent)
        Assert.assertTrue(evt.wasCalled());
        Assert.assertEquals(5, evt.getCalled());

        // Call event for client
        evt = new AnotherEvent("Client event", 123);
        manager2.callEvent(evt);

        Assert.assertTrue(evt.wasCalled());
        Assert.assertEquals(5, evt.getCalled());
    }
}
