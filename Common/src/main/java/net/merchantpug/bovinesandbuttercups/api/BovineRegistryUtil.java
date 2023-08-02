package net.merchantpug.bovinesandbuttercups.api;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.stream.Stream;

public class BovineRegistryUtil {
    public static boolean isConfiguredCowTypeInRegistry(ResourceLocation key) {
        return ConfiguredCowTypeRegistry.containsKey(key);
    }

    public static ResourceLocation getConfiguredCowTypeKey(ConfiguredCowType<?, ?> configured) {
        return ConfiguredCowTypeRegistry.getKey(configured);
    }

    public static ConfiguredCowType<?, ?> getConfiguredCowTypeFromKey(ResourceLocation key) {
        return getConfiguredCowTypeFromKey(key, Optional.empty());
    }

    public static <C extends CowTypeConfiguration> ConfiguredCowType<C, CowType<C>> getDefaultMoobloom(CowType<C> cowType) {
        return cowType.getDefaultCowType().getSecond();
    }

    public static <C extends CowTypeConfiguration> ConfiguredCowType<C, CowType<C>> getConfiguredCowTypeFromKey(ResourceLocation key, CowType<C> cowTypeFilter) {
        return getConfiguredCowTypeFromKey(key, Optional.of(cowTypeFilter));
    }

    private static <C extends CowTypeConfiguration> ConfiguredCowType<C, CowType<C>> getConfiguredCowTypeFromKey(ResourceLocation key, Optional<CowType<C>> optional) {
        Optional<ConfiguredCowType<?, ?>> cct = ConfiguredCowTypeRegistry.get(key);
        if (cct.isEmpty()) {
            throw new NullPointerException("Could not find ConfiguredCowType at location '" + key + "'.");
        }
        if (optional.isEmpty() || cct.get().cowType().equals(optional.get())) {
            return (ConfiguredCowType<C, CowType<C>>) cct.get();
        }
        BovinesAndButtercups.LOG.error("ConfiguredCowType at location " + key + " does not match mob type for entity. Expected: " + Services.PLATFORM.getCowTypeKey(optional.get()));
        return optional.get().getDefaultCowType().getSecond();
    }

    public static Stream<ConfiguredCowType<?, ?>> configuredCowTypeStream() {
        return ConfiguredCowTypeRegistry.valueStream();
    }

    public static boolean isFlowerTypeInRegistry(ResourceLocation key) {
        return FlowerTypeRegistry.containsKey(key);
    }

    public static ResourceLocation getFlowerTypeKey(FlowerType type) {
        return FlowerTypeRegistry.getKey(type);
    }

    public static FlowerType getFlowerTypeFromKey(ResourceLocation key) {
        return FlowerTypeRegistry.get(key);
    }

    public static Stream<FlowerType> flowerTypeStream() {
        return FlowerTypeRegistry.valueStream();
    }

    public static boolean isMushroomTypeInRegistry(ResourceLocation key) {
        return MushroomTypeRegistry.containsKey(key);
    }

    public static ResourceLocation getMushroomTypeKey(MushroomType type) {
        return MushroomTypeRegistry.getKey(type);
    }

    public static MushroomType getMushroomTypeFromKey(ResourceLocation key) {
        return MushroomTypeRegistry.get(key);
    }

    public static Stream<MushroomType> mushroomTypeStream() {
        return MushroomTypeRegistry.valueStream();
    }
}
