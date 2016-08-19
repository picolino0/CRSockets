package nl.colinrosen.sockets.api.shared.packets.outgoing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Colin Rosen
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PacketEnum {

    ValueType type() default ValueType.NAME;

    String method() default "";

    enum ValueType {
        NAME,
        ORDINAL,
        METHOD_VAL
    }
}
