package nl.colinrosen.sockets.api.shared.packets;

import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketException extends RuntimeException {
    private JSONObject args;

    public PacketException(JSONObject args, String message) {
        super(message);

        this.args = args;
    }

    public JSONObject getArgs() {
        return new JSONObject(args);
    }
}
