package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInPing00Pong extends PacketIn {

    public PacketInPing00Pong(JSONObject args) {
        super(PacketStage.PING, 0, args);
    }
}
