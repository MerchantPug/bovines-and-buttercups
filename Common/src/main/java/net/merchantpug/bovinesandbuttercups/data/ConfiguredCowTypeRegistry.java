package net.merchantpug.bovinesandbuttercups.data;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class ConfiguredCowTypeRegistry {
    private static final Map<ResourceLocation, ConfiguredCowType<?, ?>> LOCATION_TO_CONFIGURED_COW_TYPE = new HashMap<>();
    private static final Map<ConfiguredCowType<?, ?>, ResourceLocation> CONFIGURED_COW_TYPE_TO_LOCATION = new HashMap<>();

    public static Optional<ConfiguredCowType<?, ?>> register(ResourceLocation location, ConfiguredCowType<?, ?> value) {
        if (LOCATION_TO_CONFIGURED_COW_TYPE.containsKey(location)) {
            BovinesAndButtercups.LOG.warn("Tried registering ConfiguredCowType at location '{}'. Of which has already been registered. (Skipping).", location);
            return Optional.empty();
        }
        CONFIGURED_COW_TYPE_TO_LOCATION.put(value, location);
        return Optional.ofNullable(LOCATION_TO_CONFIGURED_COW_TYPE.put(location, value));
    }

    public static Optional<ConfiguredCowType<?, ?>> update(ResourceLocation location, ConfiguredCowType<?, ?> value) {
        LOCATION_TO_CONFIGURED_COW_TYPE.remove(location, value);
        if (CONFIGURED_COW_TYPE_TO_LOCATION.containsValue(location)) {
            CONFIGURED_COW_TYPE_TO_LOCATION.forEach((configuredCowType, location1) -> {
                if (location1 == location) {
                    CONFIGURED_COW_TYPE_TO_LOCATION.remove(configuredCowType, location);
                }
            });
        }
        return register(location, value);
    }

    public static ResourceLocation getKey(ConfiguredCowType<?, ?> value) {
        return CONFIGURED_COW_TYPE_TO_LOCATION.get(value);
    }

    public static boolean containsKey(ResourceLocation location) {
        return LOCATION_TO_CONFIGURED_COW_TYPE.containsKey(location);
    }

    public static Optional<ConfiguredCowType<?, ?>> get(ResourceLocation location) {
        return Optional.ofNullable(LOCATION_TO_CONFIGURED_COW_TYPE.get(location));
    }

    public static int size() {
        return LOCATION_TO_CONFIGURED_COW_TYPE.size();
    }

    public static Stream<ResourceLocation> keyStream() {
        return LOCATION_TO_CONFIGURED_COW_TYPE.keySet().stream();
    }

    public static Stream<ConfiguredCowType<?, ?>> valueStream() {
        return LOCATION_TO_CONFIGURED_COW_TYPE.values().stream();
    }

    public static Stream<Map.Entry<ResourceLocation, ConfiguredCowType<?, ?>>> asStream() {
        return LOCATION_TO_CONFIGURED_COW_TYPE.entrySet().stream();
    }

    public static void clear() {
        LOCATION_TO_CONFIGURED_COW_TYPE.clear();
        CONFIGURED_COW_TYPE_TO_LOCATION.clear();
        Services.PLATFORM.registerDefaultConfiguredCowTypes();
    }
}
