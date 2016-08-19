package nl.colinrosen.sockets.v1_0_0_0.server.packets.outgoing;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketEnum;
import nl.colinrosen.sockets.api.server.packets.outgoing.PacketOut;
import nl.colinrosen.sockets.api.server.packets.outgoing.TransientField;

/**
 * @author Colin Rosen
 */
public class PacketOutHandShake00HandShake extends PacketOut {
    private double num1, num2;
    @TransientField
    private double equal;
    @PacketEnum(type = PacketEnum.ValueType.ORDINAL)
    private ChallengeType type;

    public PacketOutHandShake00HandShake() {
        super(PacketStage.HANDSHAKE, 0);

        double rand = Math.random();

        type = ChallengeType.values()[(int) (Math.round(rand * 10) % ChallengeType.values().length)];
        num1 = rand * 185;
        num2 = rand * 537 / 100;

        switch (type) {
            case PLUS:
                equal = num1 + num2;
                break;
            case MIN:
                equal = num1 - num2;
                break;
            case MULTI:
                equal = num1 * num2;
                break;
            case DIV:
                equal = num1 / num2;
                break;
        }
    }

    public double getNumber1() {
        return num1;
    }

    public double getNumber2() {
        return num2;
    }

    public double getEquals() {
        return equal;
    }

    public ChallengeType getType() {
        return type;
    }

    enum ChallengeType {
        PLUS,
        MIN,
        MULTI,
        DIV;
    }
}
