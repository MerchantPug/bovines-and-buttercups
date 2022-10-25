package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.services.IDataHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesForge;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;

@AutoService(IDataHelper.class)
public class ForgeDataHelper implements IDataHelper {
    @Override
    public ResourceKey<Registry<ConfiguredCowType<?, ?>>> getConfiguredCowTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("configured_cow_type"));
    }

    @Override
    public Codec<ConfiguredCowType<?, ?>> getConfiguredCowTypeByNameCodec() {
        return BovineRegistriesForge.CONFIGURED_COW_TYPE_REGISTRY.get().getCodec();
    }

    @Override
    public ResourceKey<Registry<FlowerType>> getFlowerTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("flower_type"));
    }

    @Override
    public Codec<FlowerType> getFlowerTypeByNameCodec() {
        return BovineRegistriesForge.FLOWER_TYPE_REGISTRY.get().getCodec();
    }

    @Override
    public ResourceKey<Registry<MushroomType>> getMushroomTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("mushroom_type"));
    }

    @Override
    public Codec<MushroomType> getMushroomTypeByNameCodec() {
        return BovineRegistriesForge.MUSHROOM_TYPE_REGISTRY.get().getCodec();
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(() -> BovineRegistriesForge.COW_TYPE_REGISTRY.get().getCodec());
    }
}
