package nl.colinrosen.sockets.api.server.events.packets;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.events.Event;
import nl.colinrosen.sockets.api.server.events.HandlerList;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;

/**
 * @author Colin Rosen
 */
public class PacketReceiveEvent extends Event {
    private static HandlerList handlerlist;

    private Connection connection;
    private PacketIn packet;

    public PacketReceiveEvent(Connection connection, PacketIn packet) {
        this.connection = connection;
        this.packet = packet;
    }

    public Connection getClient() {
        return connection;
    }

    public PacketIn getPacket() {
        return packet;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
