package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutAny00Disconnect extends PacketOut {
    private String reason;

    public PacketOutAny00Disconnect(String reason) {
        super(PacketStage.ANY, 0);

        this.reason = reason;
    }
}
