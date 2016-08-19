package nl.colinrosen.sockets.api.server.events.client;

import nl.colinrosen.sockets.api.server.Connection;

/**
 * @author Colin Rosen
 *         <p>
 *         Called whenever a client disconnects
 *         </p>
 */
public class ClientDisconnectEvent extends ClientEvent {

    private String reason;

    public ClientDisconnectEvent(Connection client, String reason) {
        super(client);

        this.reason = reason;
    }

    /**
     * The reason given by the client for their disconnect or null if no reason was given
     */
    public String getReason() {
        return reason;
    }
}
