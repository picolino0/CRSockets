package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutHandshake01Response extends PacketOut {

    private boolean success;
    private String message;

    public PacketOutHandshake01Response(boolean success, String message) {
        super(PacketStage.HANDSHAKE, 1);

        this.success = success;
        this.message = message;
    }
}
