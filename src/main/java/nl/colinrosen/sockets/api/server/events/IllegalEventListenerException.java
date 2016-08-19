package nl.colinrosen.sockets.api.server.events;

/**
 * @author Colin Rosen
 */
public class IllegalEventListenerException extends RuntimeException {

    public IllegalEventListenerException(String message) {
        super(message);
    }
}
