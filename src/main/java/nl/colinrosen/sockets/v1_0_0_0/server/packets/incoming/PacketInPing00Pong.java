package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInPing00Pong implements PacketIn {

    private JSONObject args;

    public PacketInPing00Pong(JSONObject args) {
        this.args = args;
    }

    public PacketStage getStage() {
        return PacketStage.PING;
    }

    public int getID() {
        return 0;
    }

    public JSONObject getArgs() {
        return new JSONObject(args);
    }
}
