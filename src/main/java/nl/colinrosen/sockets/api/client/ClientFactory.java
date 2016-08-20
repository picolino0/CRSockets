package nl.colinrosen.sockets.api.client;

import nl.colinrosen.sockets.api.server.Server;
import nl.colinrosen.sockets.v1_0_0_0.client.CRClientFactory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Colin Rosen
 */
public abstract class ClientFactory {
    private static ClientFactory instance;
    protected final static List<Client> clients = new ArrayList<>();
    private static boolean debug = false, showErrors = false;

    protected ClientFactory() {
        instance = this;
    }

    /**
     * @return The singleton instance of the ClientFactory or creates a new one when there isn't an instance
     */
    public static ClientFactory getInstance() {
        if (instance == null)
            new CRClientFactory();

        return instance;
    }

    /**
     * Close all client that have been created by this factory
     *
     * @throws IOException if an I/O error occurs when closing the socket
     */
    public static void closeClients() throws IOException {
        for (Client cl : clients)
            cl.close();
    }

    /**
     * Alias to {@link #newClient(String, int)}
     *
     * @param address The address the new client should connect to
     * @param port    The port the new client should connect to
     */
    public static Client newClientInstance(String address, int port) throws UnknownHostException {
        return getInstance().newClient(address, port);
    }

    /**
     * Creates a new instance of Client with the given address and port
     * <p>
     * This does not start the client yet.
     * So if you get a client instance on the same address and port twice,
     * the I/O Exception will be thrown once the start() method has
     * been called on the Client instances
     *
     * @param address The address the client needs to bind to
     * @param port    The port the client needs to bind to
     */
    public abstract Client newClient(String address, int port) throws UnknownHostException;

    /**
     * Adds a client to the list, this method is protected,
     * because it should only be called when a subclass creates a new client instance
     *
     * @param client The client to add
     */
    protected final void addClient(Client client) {
        clients.add(client);
    }

    /**
     * @return if debug messages should be printed
     */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Whether or not debug message should be printed
     * @param debug
     */
    public static void doDebug(boolean debug){
        ClientFactory.debug = debug;
    }

    /**
     * @return If details about certain errors should be printed
     */
    public static boolean isShowingErrors() {
        return showErrors;
    }

    /**
     * @param show If details about certain errors should be printed
     */
    public static void doShowErrors(boolean show) {
        showErrors = show;
    }
}
