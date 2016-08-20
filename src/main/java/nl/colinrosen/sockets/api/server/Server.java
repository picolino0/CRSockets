package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.api.shared.events.EventBase;
import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public interface Server extends EventBase {

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
     * Closes the server and all connections, but sends a disconnect packet with the given <code>String reason</code> first
     * <p>
     * * Any thread currently blocked in the sockets accept method will throw
     * a {@link SocketException}.
     *
     * @param reason The reason for disconnecting
     * @throws IOException if an I/O error occurs when closing the socket
     */
    void close(String reason) throws IOException;

    /**
     * Sends a packet to all clients
     *
     * @param packet The packet to be sent
     */
    void broadcast(PacketOut packet) throws IOException;

    /**
     * Sends a notification to all connected clients
     *
     * @param notification The notification to send
     * @throws IOException if an I/O error occurs when sending a message
     */
    void broadcastNotification(String notification) throws IOException;

    /**
     * Gets the port the server is running on
     */
    int getPort();

    /**
     * @return If the server has started and is currently still running
     */
    boolean isRunning();

    /**
     * Gets the connection with the given uuid
     *
     * @param uuid The unique id of a connection
     */
    Connection getConnection(UUID uuid);

    /**
     * Gets a list of all connections
     */
    List<Connection> getConnections();
}
