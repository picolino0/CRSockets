package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInConnectedXXNotification extends PacketIn {

    private String notification;
    private JSONObject response;

    public PacketInConnectedXXNotification(JSONObject args) {
        super(PacketStage.CONNECTED, -1, args);

        if (!args.containsKey("notification") || !args.containsKey("response"))
            throw new PacketException(args, "Missing keys");

        this.notification = args.get("notification").toString();
        this.response = (JSONObject) args.get("response");
    }

    public String getNotification() {
        return notification;
    }

    public JSONObject getResponse() {
        return response;
    }
}
