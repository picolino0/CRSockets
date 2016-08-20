package connection;

import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class DummyPacket extends PacketOut {

    private String message;

    public DummyPacket(String message) {
        super(1);

        this.message = message;
    }
}
