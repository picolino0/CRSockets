package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class CRPacketIn implements PacketIn {

    private PacketStage stage;
    private int id;
    private JSONObject args;

    public CRPacketIn(PacketStage stage, int id, JSONObject args) {
        this.stage = stage;
        this.id = id;
        this.args = args;
    }

    @Override
    public PacketStage getStage() {
        return stage;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public JSONObject getArgs() {
        return args;
    }
}
