package net.merchantpug.bovinesandbuttercups.api;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;
import java.util.stream.Stream;

public class BovineRegistryUtil {
    public static boolean isConfiguredCowTypeInRegistry(LevelAccessor level, ResourceLocation key) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            BovinesAndButtercups.LOG.warn("Could not get ConfiguredCowType registry for entry check.");
            return false;
        }
        return registry.get().containsKey(key);
    }

    public static ResourceLocation getConfiguredCowTypeKey(LevelAccessor level, ConfiguredCowType<?, ?> configured) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for getting cow key from configuration.");
        }
        return registry.get().getKey(configured);
    }

    public static ConfiguredCowType<?, ?> getConfiguredCowTypeFromKey(LevelAccessor level, ResourceLocation key) {
        return getConfiguredCowTypeFromKey(level, key, Optional.empty());
    }

    public static <C extends CowTypeConfiguration, T extends CowType<C>> ConfiguredCowType<C, T> getConfiguredCowTypeFromKey(LevelAccessor level, ResourceLocation key, T cowTypeFilter) {
        return (ConfiguredCowType<C, T>) getConfiguredCowTypeFromKey(level, key, Optional.of(cowTypeFilter));
    }

    private static ConfiguredCowType<?, ?> getConfiguredCowTypeFromKey(LevelAccessor level, ResourceLocation key, Optional<CowType<?>> optional) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for getting configuration from cow key.");
        }
        if (!registry.get().containsKey(key)) {
            throw new NullPointerException("Could not find ConfiguredCowType at location " + key + ".");
        }
        ConfiguredCowType<?, ?> cct = registry.get().get(key);
        if (optional.isEmpty() || cct.getCowType().equals(optional.get())) {
            return cct;
        }
        throw new ClassCastException("ConfiguredCowType at location " + key + " does not match mob type for entity. Expected: " + level.registryAccess().registryOrThrow(BovineRegistryKeys.COW_TYPE_KEY).getKey(optional.get()));
    }

    public static Stream<ConfiguredCowType<?, ?>> configuredCowTypeStream(LevelAccessor level) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for ConfiguredCowType stream.");
        }
        return registry.get().stream();
    }

    public static boolean isFlowerTypeInRegistry(LevelAccessor level, ResourceLocation key) {
        Optional<? extends Registry<FlowerType>> registry = level.registryAccess().registry(BovineRegistryKeys.FLOWER_TYPE_KEY);
        if (registry.isEmpty()) {
            BovinesAndButtercups.LOG.warn("Could not get FlowerType registry for entry check.");
            return false;
        }
        return registry.get().containsKey(key);
    }

    public static ResourceLocation getFlowerTypeKey(LevelAccessor level, FlowerType type) {
        Optional<? extends Registry<FlowerType>> registry = level.registryAccess().registry(BovineRegistryKeys.FLOWER_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get FlowerType registry for getting key from flower type.");
        }
        return registry.get().getKey(type);
    }

    public static FlowerType getFlowerTypeFromKey(LevelAccessor level, ResourceLocation key) {
        Optional<? extends Registry<FlowerType>> registry = level.registryAccess().registry(BovineRegistryKeys.FLOWER_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get FlowerType registry for getting flower type from key.");
        }
        return registry.get().getOptional(key).orElse(FlowerType.MISSING);
    }

    public static Stream<FlowerType> flowerTypeStream(LevelAccessor level) {
        Optional<? extends Registry<FlowerType>> registry = level.registryAccess().registry(BovineRegistryKeys.FLOWER_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get FlowerType registry for getting flower type stream.");
        }
        return registry.get().stream();
    }

    public static boolean isMushroomTypeInRegistry(LevelAccessor level, ResourceLocation key) {
        Optional<? extends Registry<MushroomType>> registry = level.registryAccess().registry(BovineRegistryKeys.MUSHROOM_TYPE_KEY);
        if (registry.isEmpty()) {
            BovinesAndButtercups.LOG.warn("Could not get MushroomType registry for entry check.");
            return false;
        }
        return registry.get().containsKey(key);
    }

    public static ResourceLocation getMushroomTypeKey(LevelAccessor level, MushroomType type) {
        Optional<? extends Registry<MushroomType>> registry = level.registryAccess().registry(BovineRegistryKeys.MUSHROOM_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get MushroomType registry for getting key from mushroom type.");
        }
        return registry.get().getKey(type);
    }

    public static MushroomType getMushroomTypeFromKey(LevelAccessor level, ResourceLocation key) {
        Optional<? extends Registry<MushroomType>> registry = level.registryAccess().registry(BovineRegistryKeys.MUSHROOM_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get MushroomType registry for getting mushroom type from key.");
        }
        return registry.get().getOptional(key).orElse(MushroomType.MISSING);
    }

    public static Stream<MushroomType> mushroomTypeStream(LevelAccessor level) {
        Optional<? extends Registry<MushroomType>> registry = level.registryAccess().registry(BovineRegistryKeys.MUSHROOM_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get MushroomType registry for getting mushroom type stream.");
        }
        return registry.get().stream();
    }
}
