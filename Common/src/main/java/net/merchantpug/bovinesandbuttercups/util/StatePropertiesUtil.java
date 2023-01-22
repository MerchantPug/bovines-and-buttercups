package net.merchantpug.bovinesandbuttercups.util;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.Map;

public class StatePropertiesUtil {
    public static String statePropertiesToString(Map<Property<?>, Comparable<?>> map) {
        StringBuilder $$1 = new StringBuilder();

        for(Map.Entry<Property<?>, Comparable<?>> $$2 : map.entrySet()) {
            if ($$1.length() != 0) {
                $$1.append(',');
            }

            Property<?> $$3 = $$2.getKey();
            $$1.append($$3.getName());
            $$1.append('=');
            $$1.append(getValue($$3, $$2.getValue()));
        }

        return $$1.toString();
    }

    private static <T extends Comparable<T>> String getValue(Property<T> property, Comparable<?> comparable) {
        return property.getName((T)comparable);
    }
}
