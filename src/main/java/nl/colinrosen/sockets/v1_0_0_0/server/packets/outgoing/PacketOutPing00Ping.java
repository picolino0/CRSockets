package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutPing00Ping extends PacketOut {

    private int ping;

    public PacketOutPing00Ping(int ping) {
        super(PacketStage.PING, 0);

        this.ping = ping;
    }
}
