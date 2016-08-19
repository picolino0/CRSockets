package nl.colinrosen.sockets.api.shared.events;

/**
 * @author Colin Rosen
 *         <p>
 *         Called when an EventListener was not formatted correctly
 */
public class IllegalEventListenerException extends RuntimeException {

    public IllegalEventListenerException(String message) {
        super(message);
    }
}
