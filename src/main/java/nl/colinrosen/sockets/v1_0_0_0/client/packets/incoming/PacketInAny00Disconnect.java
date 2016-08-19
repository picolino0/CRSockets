package nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInAny00Disconnect extends PacketIn {

    private String reason;

    public PacketInAny00Disconnect(JSONObject args) {
        super(PacketStage.ANY, 0, args);

        if (!args.containsKey("reason"))
            throw new PacketException(args, "Missing 'reason' key");

        reason = args.get("reason").toString();
    }

    public String getReason() {
        return reason;
    }
}
