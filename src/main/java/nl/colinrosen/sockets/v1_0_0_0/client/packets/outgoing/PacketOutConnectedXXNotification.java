package nl.colinrosen.sockets.v1_0_0_0.client.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketOutConnectedXXNotification extends PacketOut {
    private String notification;
    private JSONObject response;

    public PacketOutConnectedXXNotification(String notification, JSONObject response) {
        super(PacketStage.CONNECTED, -1);

        this.notification = notification;
        this.response = response;
    }
}
