package nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInHandshake01Response extends PacketIn {
    private boolean success;
    private String message;

    public PacketInHandshake01Response(JSONObject args) {
        super(PacketStage.HANDSHAKE, 1, args);

        if (!args.containsKey("success") || !args.containsKey("message"))
            throw new PacketException(args, "Missing keys");

        success = args.get("success").toString().equalsIgnoreCase("true");
        message = args.get("message").toString();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
