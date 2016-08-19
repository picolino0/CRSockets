package nl.colinrosen.sockets.v1_0_0_0.client.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutHandshake00Response extends PacketOut {
    private double result;

    public PacketOutHandshake00Response(double result) {
        super(PacketStage.HANDSHAKE, 0);

        this.result = result;
    }
}
