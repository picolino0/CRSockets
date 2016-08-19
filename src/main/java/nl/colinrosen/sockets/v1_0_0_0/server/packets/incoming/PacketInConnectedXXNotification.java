package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.server.packets.PacketException;
import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInConnectedXXNotification implements PacketIn {

    private JSONObject args;
    private String notification;
    private JSONObject response;

    public PacketInConnectedXXNotification(JSONObject args) {
        this.args = args;

        if (!args.containsKey("notification") || !args.containsKey("response"))
            throw new PacketException(args, "Missing keys");

        this.notification = args.get("notification").toString();
        this.response = (JSONObject) args.get("response");
    }

    public PacketStage getStage() {
        return PacketStage.CONNECTED;
    }

    public int getID() {
        return -1;
    }

    public JSONObject getArgs() {
        return args;
    }

    public String getNotification(){
        return notification;
    }

    public JSONObject getResponse(){
        return response;
    }
}
