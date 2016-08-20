package connection;

import nl.colinrosen.sockets.api.client.Client;
import nl.colinrosen.sockets.api.client.events.ConnectionEvent;
import nl.colinrosen.sockets.api.client.events.KickedEvent;
import nl.colinrosen.sockets.api.client.events.NotificationEvent;
import nl.colinrosen.sockets.api.client.events.PacketReceiveEvent;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.events.client.ClientNotificationEvent;
import nl.colinrosen.sockets.api.shared.events.EventHandler;
import nl.colinrosen.sockets.api.shared.events.EventListener;
import nl.colinrosen.sockets.api.shared.packets.PacketException;
import org.json.simple.JSONObject;

import java.io.IOException;

/**
 * @author Colin Rosen
 */
public class Events implements EventListener {

    private volatile boolean doingStuff = true;
    private Server serv;
    private Client client;

    public Events(Server serv, Client client) {
        this.serv = serv;
        this.client = client;
    }

    //region Client events
    @EventHandler
    public void onPacketClient(PacketReceiveEvent evt) {
        System.out.println("New packet:");
        System.out.println("Stage: " + evt.getPacket().getStage().name());
        System.out.println("ID: " + evt.getPacket().getID());
        System.out.println("Args: " + evt.getPacket().getArgs().toJSONString());
        System.out.println("--");

        doingStuff = false;
    }

    @EventHandler
    public void onConnectClient(ConnectionEvent evt) {
        System.out.println("Connected as " + evt.getUUID());
        System.out.println("--");

        try {
            serv.broadcastNotification("Hello World!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onNotify(NotificationEvent evt) {
        System.out.println("Notification:");
        System.out.println(evt.getNotification());

        if (!evt.getNotification().equals("Hello World!"))
            throw new PacketException(null, "Invalid notification value");

        JSONObject resp = new JSONObject();
        resp.put("Hello", "World");
        evt.setResponse(resp);

        System.out.println("--");
    }

    @EventHandler
    public void onKick(KickedEvent evt){
        System.out.println("Kicked with reason:");
        System.out.println(evt.getReason());

        if (!"End of test".equals(evt.getReason()))
            throw new PacketException(null, "Wrong kick reason");
    }
    //endregion

    //region Server events
    @EventHandler
    public void onPacketServer(nl.colinrosen.sockets.api.server.events.packets.PacketReceiveEvent evt) {
        System.out.println("New packet (server):");
        System.out.println("Stage: " + evt.getPacket().getStage().name());
        System.out.println("ID: " + evt.getPacket().getID());
        System.out.println("Args: " + evt.getPacket().getArgs().toJSONString());
        System.out.println("Client: " + evt.getClient().getID());
        System.out.println("--");
    }

    @EventHandler
    public void onNotifyResponse(ClientNotificationEvent evt) {
        System.out.println("Notification: " + evt.getNotification());
        System.out.println("Response: " + evt.getResponse().toJSONString());

        if (!evt.getResponse().containsKey("Hello") || !evt.getResponse().get("Hello").toString().equals("World"))
            throw new PacketException(evt.getResponse(), "Invalid notification response");

        System.out.println("--");

        DummyPacket pack = new DummyPacket("Buenos dias =)");
        try {
            serv.broadcast(pack);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    //endregion

    public boolean isDoingStuff() {
        return doingStuff;
    }
}
