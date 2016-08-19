package nl.colinrosen.sockets.v1_0_0_0.client.packets.incoming;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import nl.colinrosen.sockets.api.shared.packets.incoming.PacketIn;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public class PacketInHandshake00Handshake extends PacketIn {

    private double num1, num2, equals;
    private ChallengeType type;

    public PacketInHandshake00Handshake(JSONObject args) {
        super(PacketStage.HANDSHAKE, 0, args);

        if (!args.containsKey("num1") || !args.containsKey("num2") || !args.containsKey("type"))
            throw new PacketException(args, "Missing keys");

        try {
            num1 = Double.parseDouble(args.get("num1").toString());
            num2 = Double.parseDouble(args.get("num2").toString());
            type = ChallengeType.fromOrdinal(Integer.parseInt(args.get("type").toString()));
        } catch (NumberFormatException ex) {
            throw new PacketException(args, "Invalid keys");
        }

        switch (type) {
            case PLUS:
                equals = num1 + num2;
                break;
            case MIN:
                equals = num1 - num2;
                break;
            case MULTI:
                equals = num1 * num2;
                break;
            case DIV:
                equals = num1 / num2;
                break;
        }
    }

    public double getResult() {
        return equals;
    }

    enum ChallengeType {
        PLUS,
        MIN,
        MULTI,
        DIV;

        static ChallengeType fromOrdinal(int ordinal) {
            if (ordinal < 0 || ordinal >= values().length)
                return null;

            return values()[ordinal];
        }
    }
}
