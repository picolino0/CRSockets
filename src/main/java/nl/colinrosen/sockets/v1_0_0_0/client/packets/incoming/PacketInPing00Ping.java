package nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInPing00Ping extends PacketIn {

    private int ping;

    public PacketInPing00Ping(JSONObject args) {
        super(PacketStage.PING, 0, args);

        if (!args.containsKey("ping"))
            throw new PacketException(args, "Missing 'ping' key");

        try {
            ping = Integer.parseInt(args.get("ping").toString());
        } catch (NumberFormatException ex) {
            throw new PacketException(args, "Invalid 'ping' key");
        }
    }

    public int getPing() {
        return ping;
    }
}
