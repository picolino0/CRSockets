package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutPing00Ping extends PacketOut {

    public PacketOutPing00Ping() {
        super(PacketStage.PING, 0);
    }
}
