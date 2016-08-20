package nl.colinrosen.sockets.v1_0_0_0.server;

import java.io.IOException;

/**
 * @author Colin Rosen
 */
public class ClientPing {

    private CRConnection conn;
    private long timepassed, prevTime;
    private int ping;

    public ClientPing(CRConnection conn) {
        this.conn = conn;
        prevTime = System.currentTimeMillis();
    }

    public int getPing() {
        return ping;
    }

    public void updateTimePassed() {
        // Update timepassed and previous time
        timepassed += System.currentTimeMillis() - prevTime;
        prevTime = System.currentTimeMillis();

        // If more time has passed than is allowed, close the connection
        if (timepassed >= conn.getTimeout()) {
            try {
                conn.close("Timed out");
            } catch (IOException ex) {
                // Already closed
            }
        }
    }

    public void refresh() {
        ping = (int) timepassed;

        timepassed = 0;
        prevTime = System.currentTimeMillis();
    }
}
