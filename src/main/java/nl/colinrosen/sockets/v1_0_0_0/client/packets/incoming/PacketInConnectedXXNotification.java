package nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInConnectedXXNotification extends PacketIn {

    private String notification;

    public PacketInConnectedXXNotification(JSONObject args) {
        super(PacketStage.CONNECTED, -1, args);

        if (!args.containsKey("notification"))
            throw new PacketException(args, "Missing 'notification' key");

        notification = args.get("notification").toString();
    }

    public String getNotification() {
        return notification;
    }
}
