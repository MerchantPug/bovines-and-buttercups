package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class BovineRegistryKeys {
    public static final ResourceKey<Registry<CowType<?>>> COW_TYPE_KEY = ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("cow_type"));
    public static final ResourceKey<Registry<ConfiguredCowType<?, ?>>> CONFIGURED_COW_TYPE_KEY = Services.DATA.getConfiguredCowTypeResourceKey();
    public static final ResourceKey<Registry<FlowerType>> FLOWER_TYPE_KEY = Services.DATA.getFlowerTypeResourceKey();
    public static final ResourceKey<Registry<MushroomType>> MUSHROOM_TYPE_KEY = Services.DATA.getMushroomTypeResourceKey();
}
