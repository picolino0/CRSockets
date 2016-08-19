package nl.colinrosen.sockets.api.client.events;

import nl.colinrosen.sockets.api.shared.events.Cancellable;
import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;

/**
 * @author Colin Rosen
 */
public class PacketReceiveEvent extends Event implements Cancellable {

    private static HandlerList handlerlist;

    private PacketIn packet;
    private boolean cancelled;

    public PacketReceiveEvent(PacketIn packet) {
        this.packet = packet;
    }

    public PacketIn getPacket() {
        return packet;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
