package nl.colinrosen.sockets.api.server;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public interface Connection {

    /**
     * The unique id of the connection.
     * Automatically generated
     */
    UUID getID();

    /**
     * Whether the connection is opened or not
     */
    boolean isConnected();

    /**
     * Sends a string directly to the client
     *
     * @param message The message to be sent
     */
    void sendRaw(String message);

    /**
     * Close the connection
     */
    void close();

    /**
     * Close the connection with the given reason
     *
     * @param reason The reason for closing the connection
     */
    void close(String reason);

    /**
     * Get the time it takes to send and receive a ping message
     */
    int getPing();

    /**
     * If the specified amount of time passes without receiving an object over TCP, the connection is considered closed
     */
    long getTimeout();

    /**
     * Sets the timeout time (see {@link #getTimeout() getTimeout()})
     *
     * @param time
     */
    void setTimeout(long time);

    /**
     * The InetSocketAddress of the connection
     */
    InetSocketAddress getAddress();
}
