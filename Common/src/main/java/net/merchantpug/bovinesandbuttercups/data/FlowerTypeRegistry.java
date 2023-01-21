package net.merchantpug.bovinesandbuttercups.data;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class FlowerTypeRegistry {
    private static final Map<ResourceLocation, FlowerType> LOCATION_TO_FLOWER_TYPE = new HashMap<>();
    private static final Map<FlowerType, ResourceLocation> FLOWER_TYPE_TO_LOCATION = new HashMap<>();

    public static Optional<FlowerType> register(ResourceLocation location, FlowerType value) {
        if (LOCATION_TO_FLOWER_TYPE.containsKey(location)) {
            BovinesAndButtercups.LOG.warn("Tried registering FlowerType at location '{}'. Of which has already been registered. (Skipping).", location);
            return Optional.empty();
        }
        FLOWER_TYPE_TO_LOCATION.put(value, location);
        return Optional.ofNullable(LOCATION_TO_FLOWER_TYPE.put(location, value));
    }

    public static Optional<FlowerType> update(ResourceLocation location, FlowerType value) {
        LOCATION_TO_FLOWER_TYPE.remove(location, value);
        if (FLOWER_TYPE_TO_LOCATION.containsValue(location)) {
            FLOWER_TYPE_TO_LOCATION.forEach((configuredCowType, location1) -> {
                if (location1 == location) {
                    FLOWER_TYPE_TO_LOCATION.remove(configuredCowType, location);
                }
            });
        }
        return register(location, value);
    }

    public static ResourceLocation getKey(FlowerType value) {
        return FLOWER_TYPE_TO_LOCATION.get(value);
    }

    public static boolean containsKey(ResourceLocation location) {
        return LOCATION_TO_FLOWER_TYPE.containsKey(location);
    }

    public static FlowerType get(ResourceLocation location) {
        return LOCATION_TO_FLOWER_TYPE.get(location);
    }

    public static int size() {
        return LOCATION_TO_FLOWER_TYPE.size();
    }

    public static Stream<FlowerType> valueStream() {
        return LOCATION_TO_FLOWER_TYPE.values().stream();
    }

    public static Stream<Map.Entry<ResourceLocation, FlowerType>> asStream() {
        return LOCATION_TO_FLOWER_TYPE.entrySet().stream();
    }

    public static void clear() {
        LOCATION_TO_FLOWER_TYPE.clear();
        FLOWER_TYPE_TO_LOCATION.clear();
        register(BovinesAndButtercups.asResource("missing_flower"), FlowerType.MISSING);
    }
}
