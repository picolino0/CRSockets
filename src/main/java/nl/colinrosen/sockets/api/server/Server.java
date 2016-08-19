package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.api.server.events.EventManager;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;

import java.io.IOException;
import java.net.SocketException;

/**
 * @author Colin Rosen
 */
public interface Server {

    /**
     * Start the server
     *
     * @throws IOException              if an I/O error occurs when opening the socket
     * @throws IllegalArgumentException if the port parameter is outside
     *                                  the specified range of valid port values, which is between
     *                                  0 and 65535, inclusive.
     */
    void start() throws IOException;

    /**
     * Closes the server and all connections
     * <p>
     * * Any thread currently blocked in the sockets accept method will throw
     * a {@link SocketException}.
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    void close() throws IOException;

    /**
     * Sends a packet to all clients
     *
     * @param packet The packet to be sent
     */
    void broadcast(PacketOut packet);

    /**
     * Gets the EventManager associated with this server
     */
    EventManager getEventManager();

    /**
     * Gets the port the server is running on
     */
    int getPort();
}
