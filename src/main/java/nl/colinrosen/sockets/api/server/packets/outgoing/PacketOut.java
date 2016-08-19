package nl.colinrosen.sockets.api.server.packets.outgoing;

import nl.colinrosen.sockets.api.server.packets.PacketException;
import nl.colinrosen.sockets.api.server.packets.PacketStage;

/**
 * @author Colin Rosen
 */
public abstract class PacketOut {

    protected int id;
    private PacketStage stage = PacketStage.CONNECTED;

    public PacketOut(int id) {
        if (id < 0)
            throw new PacketException(null, "Id must be 0 or bigger");
        this.id = id;
    }

    public PacketOut(PacketStage stage, int id) {
        this.id = id;
        this.stage = stage;
    }

    public int getID() {
        return id;
    }

    public PacketStage getStage() {
        return stage;
    }
}
