package nl.colinrosen.sockets.api.server;

import java.net.InetSocketAddress;
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
     * Whether the client is opened or not
     */
    boolean isConnected();

    /**
     * Sends a string directly to the client
     *
     * @param message The message to be sent
     */
    void sendRaw(String message);

    /**
     * Close the client
     */
    void close();

    /**
     * Close the client with the given reason
     *
     * @param reason The reason for closing the client
     */
    void close(String reason);

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
    InetSocketAddress getAddress();

    /**
     * Gets the server to which this connection belongs
     */
    Server getServer();
}
