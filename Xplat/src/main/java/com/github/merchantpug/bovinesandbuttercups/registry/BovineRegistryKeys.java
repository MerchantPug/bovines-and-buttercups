package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.api.CowType;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovineRegistryKeys {
    public static final ResourceKey<Registry<CowType<?>>> COW_TYPE_KEY = ResourceKey.createRegistryKey(Constants.resourceLocation("cow_type"));
    public static final ResourceKey<Registry<ConfiguredCowType<?, ?>>> CONFIGURED_COW_TYPE_KEY = Services.PLATFORM.getConfiguredCowTypeResourceKey();
}
