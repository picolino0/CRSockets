package nl.colinrosen.sockets.api.client;

import nl.colinrosen.sockets.api.shared.events.EventBase;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public interface Client extends EventBase {

    /**
     * The unique id of the client on the server
     */
    UUID getID();

    /**
     * Connect to the server and start listening for messages
     *
     * @throws IOException if an I/O error occurs when opening the socket
     */
    void start() throws IOException;

    /**
     * Close the connection to the server
     *
     * @throws IOException if an I/O error occures when closing the socket
     */
    void close() throws IOException;

    /**
     * Sends a packet to the server
     *
     * @param packet The packet to be sent
     * @throws IOException if an I/O error occurs when writing to the socket
     */
    void sendPacket(PacketOut packet) throws IOException;

    /**
     * Get the address of the server the client is connected to
     */
    SocketAddress getAddress();

    /**
     * True if the handshake is complete and the client is still connected, false otherwise
     */
    boolean isConnected();

    /**
     * Gets the ping. The amount of time it takes to send a packet from the server to the client and back
     */
    int getPing();
}
