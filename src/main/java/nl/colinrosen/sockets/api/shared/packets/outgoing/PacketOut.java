package nl.colinrosen.sockets.api.shared.packets.outgoing;

import nl.colinrosen.sockets.api.shared.packets.PacketException;
import nl.colinrosen.sockets.api.shared.packets.PacketStage;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Colin Rosen
 */
public abstract class PacketOut {

    protected int id;
    private PacketStage stage = PacketStage.CONNECTED;

    public PacketOut(int id) {
        if (id < 0)
            throw new PacketException(null, "Id must be 0 or bigger");
        this.id = id;
    }

    public PacketOut(PacketStage stage, int id) {
        this.id = id;
        this.stage = stage;
    }

    public int getID() {
        return id;
    }

    public PacketStage getStage() {
        return stage;
    }

    public final JSONObject serialize() {
        // Get all fields in the packet
        Field[] publicFields = getClass().getFields();
        Field[] privateFields = getClass().getDeclaredFields();

        // Add all fields to a single hashset
        Set<Field> fields = new HashSet<>(publicFields.length + privateFields.length, 1);
        for (Field f : publicFields)
            fields.add(f);
        for (Field f : privateFields)
            fields.add(f);

        // Create json object for packet
        JSONObject obj = new JSONObject();
        obj.put("stage", stage.name());
        obj.put("id", id);

        // Compile fields in a json object
        JSONObject args = new JSONObject();
        for (Field f : fields) {
            TransientField trans = f.getAnnotation(TransientField.class);
            if (trans != null || f.isSynthetic())
                // Ignore field if it is marked transient or if the field is synthetic
                continue;

            try {
                f.setAccessible(true);
                Object value = f.get(this);

                // Determine how to serialize an enum field
                if (value != null && Enum.class.isAssignableFrom(f.getType())) {
                    PacketEnum.ValueType type = PacketEnum.ValueType.NAME;
                    String method = "";

                    PacketEnum pe = f.getAnnotation(PacketEnum.class);
                    if (pe != null) {
                        type = pe.type();
                        method = pe.method();
                    }

                    if (type == PacketEnum.ValueType.NAME)
                        value = ((Enum) value).name();
                    if (type == PacketEnum.ValueType.ORDINAL)
                        value = ((Enum) value).ordinal();
                    if (type == PacketEnum.ValueType.METHOD_VAL) {
                        try {
                            value = value.getClass().getMethod(method).invoke(value);
                        } catch (NoSuchMethodException | InvocationTargetException ex) {
                            // Reset value if method didn't exist
                            value = f.get(this);
                        }
                    }
                }

                args.put(f.getName(), value);
            } catch (IllegalAccessException ex) {
                // Ignore
            }
        }

        obj.put("args", args);

        return obj;
    }
}
