package nl.colinrosen.sockets.v1_0_0_0.server.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInHandShake00Response extends PacketIn {

    private double result;

    public PacketInHandShake00Response(JSONObject args) {
        super(PacketStage.HANDSHAKE, 0, args);

        if (!args.containsKey("result"))
            throw new PacketException(args, "Missing 'Result' key");

        try {
            result = Double.parseDouble(args.get("result").toString());
        } catch (NumberFormatException ex) {
            throw new PacketException(args, "Invalid 'Result' value");
        }
    }

    public double getResult() {
        return result;
    }
}
