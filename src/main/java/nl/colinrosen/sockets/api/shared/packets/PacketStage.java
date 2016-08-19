package nl.colinrosen.sockets.api.shared.packets;

/**
 * @author Colin Rosen
 */
public enum PacketStage {
    PING,
    HANDSHAKE,
    CONNECTED,
    ANY;

    public static PacketStage fromString(String name) {
        for (PacketStage stage : values())
            if (stage.name().equalsIgnoreCase(name))
                return stage;

        return null;
    }
}
