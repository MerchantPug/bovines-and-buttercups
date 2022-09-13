package com.github.merchantpug.bovinesandbuttercups.util;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;
import java.util.stream.Stream;

public class ConfiguredCowTypeRegistryUtil {
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
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for getting configuration from cow key.");
        }
        return registry.get().get(key);
    }

    public static Stream<ConfiguredCowType<?, ?>> configuredCowTypeStream(LevelAccessor level) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for ConfiguredCowType stream.");
        }
        return registry.get().stream();
    }
}
