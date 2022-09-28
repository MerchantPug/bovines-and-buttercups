package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovineRegistryKeys {
    public static final ResourceKey<Registry<CowType<?>>> COW_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("cow_type"));
    public static final ResourceKey<Registry<ConfiguredCowType<?, ?>>> CONFIGURED_COW_TYPE_KEY = Services.PLATFORM.getConfiguredCowTypeResourceKey();
}
