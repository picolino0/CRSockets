package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing.PacketOutPing00Ping;

import java.io.IOException;

/**
 * @author Colin Rosen
 */
public class PingHeartbeat implements Runnable {

    private CRServer server;

    public PingHeartbeat(CRServer server) {
        this.server = server;

        new Thread(this).start();
    }

    public void run() {
        while (server.isRunning()) {
            for (Connection iconn : server.getConnections()) {
                CRConnection conn = (CRConnection) iconn;
                conn.getClientPing().updateTimePassed();

                // Send ping to connection
                try {
                    PacketOutPing00Ping pingPack = new PacketOutPing00Ping(conn.getPing());
                    conn.sendPacket(pingPack);
                } catch (IOException ex) {
                    // Connection closed
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                // Interrupted
                break;
            }
        }
    }
}
