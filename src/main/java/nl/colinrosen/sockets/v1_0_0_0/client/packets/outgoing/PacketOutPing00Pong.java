package nl.colinrosen.sockets.v1_0_0_0.client.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutPing00Pong extends PacketOut {

    public PacketOutPing00Pong() {
        super(PacketStage.PING, 0);
    }
}
