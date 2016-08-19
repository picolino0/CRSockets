package nl.colinrosen.sockets.api.client.events;

import nl.colinrosen.sockets.api.shared.events.Event;
import nl.colinrosen.sockets.api.shared.events.HandlerList;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when the client receives a notification from the server
 */
public class NotificationEvent extends Event {
    private static HandlerList handlerlist;

    private String notification;
    private JSONObject response;

    public NotificationEvent(String notification) {
        this.notification = notification;
    }

    public String getNotification() {
        return notification;
    }

    public JSONObject getResponse() {
        return response;
    }

    public void setResponse(JSONObject obj) {
        response = obj;
    }

    public HandlerList getHandlers() {
        return handlerlist;
    }
}
