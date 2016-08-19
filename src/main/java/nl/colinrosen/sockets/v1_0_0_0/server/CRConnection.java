package nl.colinrosen.sockets.v1_0_0_0.server;

import nl.colinrosen.sockets.api.server.Connection;
import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.api.server.packets.incoming.PacketIn;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.UUID;

/**
 * @author Colin Rosen
 */
public class CRConnection implements Connection, Runnable {

    private CRServer server;
    private Socket socket;
    private UUID id;
    private BufferedReader in;
    private BufferedWriter out;
    private long timeout = 26000, timepassed;
    private int ping;
    private boolean connected;
    private boolean closed;

    public CRConnection(CRServer server, Socket socket) {
        this.server = server;
        this.socket = socket;

        id = UUID.randomUUID();

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread th = new Thread(this);
        th.start();

        Thread timer = new Thread(() -> {
            long prevTime = System.currentTimeMillis();
            while (!closed) {
                // Update timepassed and previous time
                timepassed += System.currentTimeMillis() - prevTime;
                prevTime = System.currentTimeMillis();

                // If more time has passed than is allowed, close the connection
                if (timepassed >= timeout) {
                    try {
                        close();
                    } catch (IOException ex) {
                        break;
                    }
                }

                // Wait one millisecond before repeating
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    // Interrupted
                }
            }
        });

        // timer.start();
    }

    public void run() {
        while (!closed) {
            try {
                String line = in.readLine();
                if (line == null)
                    break;

                // Set time since last message to 0
                timepassed = 0;

                // TODO: Parse packet
            } catch (IOException ex) {
                break;
            }
        }

        try {
            close();
        } catch (IOException ex) {
            // Already closed
        }
    }

    private void handlePacket(PacketIn packet) {
        // TODO: Handle packets
    }

    public UUID getID() {
        return id;
    }

    public boolean isConnected() {
        return connected;
    }

    public void sendRaw(String message) throws IOException {
        out.write(message + "\n");
        out.flush();
    }

    public void close() throws IOException {
        if (closed)
            return;

        closed = true;
        server.removeConnection(this);
        socket.close();
    }

    public void close(String reason) throws IOException {
        if (closed)
            return;

        // TODO: Do close packet stuff...

        close();
    }

    public int getPing() {
        return ping;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long time) {
        timeout = time;
    }

    public SocketAddress getAddress() {
        return socket.getRemoteSocketAddress();
    }

    public Server getServer() {
        return server;
    }
}
