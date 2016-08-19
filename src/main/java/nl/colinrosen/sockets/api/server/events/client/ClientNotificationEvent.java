package nl.colinrosen.sockets.api.server.events.client;

import nl.colinrosen.sockets.api.server.Connection;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when a client responds to a notification
 *         </p>
 */
public class ClientNotificationEvent extends ClientEvent {
    private JSONObject response;

    public ClientNotificationEvent(Connection client, JSONObject response) {
        super(client);

        this.response = response;
    }

    public JSONObject getResponse() {
        return response;
    }
}
