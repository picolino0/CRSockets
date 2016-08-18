package nl.colinrosen.sockets;

import nl.colinrosen.sockets.packets.outgoing.PacketOut;

/**
 * @author Colin Rosen
 */
public interface Server {

    void start();

    void close();

    void broadcast(PacketOut packet);
}
