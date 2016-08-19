package nl.colinrosen.sockets.api.shared.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public abstract class PacketIn {

    private PacketStage stage;
    private int id;
    private JSONObject args;

    public PacketIn(PacketStage stage, int id, JSONObject args) {
        this.stage = stage;
        this.id = id;
        this.args = args;
    }

    public final PacketStage getStage() {
        return stage;
    }

    public final int getID() {
        return id;
    }

    public final JSONObject getArgs() {
        return args;
    }
}
