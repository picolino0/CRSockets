package nl.colinrosen.sockets.api.server.events.client;

import nl.colinrosen.sockets.api.server.Connection;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when a client connects, before the handshake
 *         </p>
 */
public class ClientConnectEvent extends ClientEvent {

    private String disallowReason;

    public ClientConnectEvent(Connection client) {
        super(client);
    }

    /**
     * Don't allow the client to connect
     *
     * @param reason The reason the client isn't allowed to connect
     */
    public void disallow(String reason) {
        disallowReason = reason;
    }

    /**
     * Checks if the client has been disallowed connection
     */
    public boolean isDisallowed() {
        return disallowReason != null;
    }

    /**
     * @return The reason the client was disallowed
     */
    public String getReason(){
        return disallowReason;
    }
}
