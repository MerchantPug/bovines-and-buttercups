package net.merchantpug.bovinesandbuttercups.data;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class MushroomTypeRegistry {
    private static final Map<ResourceLocation, MushroomType> LOCATION_TO_MUSHROOM_TYPE = new HashMap<>();
    private static final Map<MushroomType, ResourceLocation> MUSHROOM_TYPE_TO_LOCATION = new HashMap<>();

    public static Optional<MushroomType> register(ResourceLocation location, MushroomType value) {
        if (LOCATION_TO_MUSHROOM_TYPE.containsKey(location)) {
            BovinesAndButtercups.LOG.warn("Tried registering FlowerType at location '{}'. Of which has already been registered. (Skipping).", location);
            return Optional.empty();
        }
        MUSHROOM_TYPE_TO_LOCATION.put(value, location);
        return Optional.ofNullable(LOCATION_TO_MUSHROOM_TYPE.put(location, value));
    }

    public static Optional<MushroomType> update(ResourceLocation location, MushroomType value) {
        LOCATION_TO_MUSHROOM_TYPE.remove(location, value);
        if (MUSHROOM_TYPE_TO_LOCATION.containsValue(location)) {
            MUSHROOM_TYPE_TO_LOCATION.forEach((configuredCowType, location1) -> {
                if (location1 == location) {
                    MUSHROOM_TYPE_TO_LOCATION.remove(configuredCowType, location);
                }
            });
        }
        return register(location, value);
    }

    public static ResourceLocation getKey(MushroomType value) {
        return MUSHROOM_TYPE_TO_LOCATION.get(value);
    }

    public static boolean containsKey(ResourceLocation location) {
        return LOCATION_TO_MUSHROOM_TYPE.containsKey(location);
    }

    public static MushroomType get(ResourceLocation location) {
        return LOCATION_TO_MUSHROOM_TYPE.get(location);
    }


    public static int size() {
        return LOCATION_TO_MUSHROOM_TYPE.size();
    }

    public static Stream<MushroomType> valueStream() {
        return LOCATION_TO_MUSHROOM_TYPE.values().stream();
    }

    public static Stream<Map.Entry<ResourceLocation, MushroomType>> asStream() {
        return LOCATION_TO_MUSHROOM_TYPE.entrySet().stream();
    }

    public static void clear() {
        LOCATION_TO_MUSHROOM_TYPE.clear();
        MUSHROOM_TYPE_TO_LOCATION.clear();
        register(BovinesAndButtercups.asResource("missing_mushroom"), MushroomType.MISSING);
    }
}
