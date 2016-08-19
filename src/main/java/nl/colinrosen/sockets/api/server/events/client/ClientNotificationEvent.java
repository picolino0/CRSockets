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
    private String notification;
    private JSONObject response;

    public ClientNotificationEvent(Connection client, String notification, JSONObject response) {
        super(client);

        this.notification = notification;
        this.response = response;
    }

    /**
     * The response the client returned after receiving {@link #notification}
     *
     * @return
     */
    public JSONObject getResponse() {
        return response;
    }

    /**
     * The notification that was sent to the client that triggered the client to send the response
     */
    public String getNotification() {
        return notification;
    }

}
