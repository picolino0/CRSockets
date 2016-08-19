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
    }

    public int getPing() {
        return ping;
    }

    public void updateTimePassed() {
        // Update timepassed and previous time
        timepassed += System.nanoTime() - prevTime;
        prevTime = System.currentTimeMillis();

        // If more time has passed than is allowed, close the connection
        if (timepassed / 1000000 >= conn.getTimeout()) {
            try {
                conn.close("Timed out");
            } catch (IOException ex) {
                // Already closed
            }
        }
    }

    public void refresh() {
        ping = (int) (timepassed / 1000000);

        timepassed = 0;
        prevTime = System.nanoTime();
    }
}
