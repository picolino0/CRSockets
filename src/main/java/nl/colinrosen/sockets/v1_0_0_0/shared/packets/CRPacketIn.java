package nl.colinrosen.sockets.v1_0_0_0.shared.packets;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class CRPacketIn extends PacketIn {

    public CRPacketIn(PacketStage stage, int id, JSONObject args) {
        super(stage, id, args);
    }
}
