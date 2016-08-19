package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public class PacketOutConnectedXXNotification extends PacketOut {
    private String notification;

    public PacketOutConnectedXXNotification(String notification) {
        super(PacketStage.CONNECTED, -1);

        this.notification = notification;
    }
}
