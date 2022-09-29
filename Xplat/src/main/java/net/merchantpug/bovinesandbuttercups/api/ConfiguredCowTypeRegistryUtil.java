package net.merchantpug.bovinesandbuttercups.api;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
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
        if (optional.isEmpty() || cct.getCowType() == optional.get()) {
            return cct;
        }
        throw new ClassCastException("ConfiguredCowType at location " + key + " does not match mob type for entity. Expected: " + BovineRegistryKeys.COW_TYPE_KEY);
    }

    public static Stream<ConfiguredCowType<?, ?>> configuredCowTypeStream(LevelAccessor level) {
        Optional<? extends Registry<ConfiguredCowType<?, ?>>> registry = level.registryAccess().registry(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY);
        if (registry.isEmpty()) {
            throw new NullPointerException("Could not get ConfiguredCowType registry for ConfiguredCowType stream.");
        }
        return registry.get().stream();
    }
}
