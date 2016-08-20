package nl.colinrosen.sockets.api.server;

import nl.colinrosen.sockets.api.shared.packets.outgoing.PacketOut;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public interface Connection {

    /**
     * The unique id of the client.
     * Automatically generated
     */
    UUID getID();

    /**
     * True if the handshake is complete and the client is still connected, false otherwise
     */
    boolean isConnected();

    /**
     * When a custom handshake is enabled. You can set the connected state of the connection
     *
     * @param connected
     * @throws IllegalAccessError When the method is called, while a custom handshake is not enabled
     */
    void setConnected(boolean connected);

    /**
     * Sends a packet to the client
     *
     * @param pack The packet to send
     * @throws IOException if an I/O error occurs when sending the packet
     */
    void sendPacket(PacketOut pack) throws IOException;

    /**
     * Send a notification to the client
     *
     * @param notification The name of the notification to send
     * @throws IOException if an I/O error occurs when sending the message
     */
    void sendNotification(String notification) throws IOException;

    /**
     * Close the client
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    void close() throws IOException;

    /**
     * Close the client with the given reason
     *
     * @param reason The reason for closing the client
     * @throws IOException if an I/O error occurs when closing the socket
     */
    void close(String reason) throws IOException;

    /**
     * Get the time it takes to send and receive a ping message
     */
    int getPing();

    /**
     * If the specified amount of time passes without receiving an object over TCP, the client is considered closed
     */
    long getTimeout();

    /**
     * Sets the timeout time (see {@link #getTimeout() getTimeout()})
     *
     * @param time the amount of milliseconds the connection is allowed to be inactive
     */
    void setTimeout(long time);

    /**
     * The InetSocketAddress of the client
     */
    SocketAddress getAddress();

    /**
     * Gets the server to which this connection belongs
     */
    Server getServer();
}
