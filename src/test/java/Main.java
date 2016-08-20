import connection.DummyPacket;
import connection.Events;
import events.*;
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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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
        ServerFactory.doDebug(true);
        ClientFactory.doDebug(true);

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
        manager.registerListener(new EventHandleTest());
        manager.registerListener(new EventHandleTestSecondary());

        // Register listener for client event manager too
        manager2.registerListener(new EventHandleTest());

        // Call first event and check if it is executed in the right order
        manager.callEvent(new SomeEvent("First event"));
        System.out.println();

        // Call second event
        AnotherEvent evt = new AnotherEvent("Second event", 42);
        manager.callEvent(evt);
        System.out.println();

        // Check if the new method was called in addition to the other methods (because AnotherEvent is a subclass of SomeEvent)
        Assert.assertTrue(evt.wasCalled());
        Assert.assertEquals(5, evt.getCalled());

        // Call event for client
        SeparateEvent evt2 = new SeparateEvent("Client event");
        manager2.callEvent(evt2);

        Assert.assertEquals(1, evt2.getCallCount());
    }

    @Test
    public void connectionTest() throws IOException {
        Events ev = new Events(serv, client);

        serv.start();
        serv.getEventManager().registerListener(ev);

        client.start();
        client.getEventManager().registerListener(ev);

        // Wait till all events are handled
        while (ev.isDoingStuff()) ;

        // Wait a second for ping to update
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int ping = serv.getConnections().get(0).getPing();
        System.out.println("Ping: " + ping); // Probably 0, because the server and client are running on the same machine

        serv.close("End of test");
    }
}
