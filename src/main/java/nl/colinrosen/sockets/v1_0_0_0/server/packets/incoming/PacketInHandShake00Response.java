package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.server.packets.PacketException;
import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInHandShake00Response implements PacketIn {

    private JSONObject args;
    private double result;

    public PacketInHandShake00Response(JSONObject args) {
        this.args = args;

        if (!args.containsKey("result"))
            throw new PacketException(args, "Missing 'Result' key");

        try {
            result = (double) args.get("result");
        } catch (ClassCastException ex) {
            throw new PacketException(args, "Invalid 'Result' value");
        }
    }

    public PacketStage getStage() {
        return PacketStage.HANDSHAKE;
    }

    public int getID() {
        return 0;
    }

    public JSONObject getArgs() {
        return new JSONObject(args);
    }

    public double getResult() {
        return result;
    }
}
