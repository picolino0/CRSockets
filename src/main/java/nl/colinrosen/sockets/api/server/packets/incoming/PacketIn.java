package nl.colinrosen.sockets.api.server.packets.incoming;

import nl.colinrosen.sockets.api.server.packets.PacketStage;
import org.json.simple.JSONObject;

/**
 * @author Colin Rosen
 */
public interface PacketIn {

    PacketStage getStage();

    int getID();

    JSONObject getArgs();
}
