package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.platform.services.IDataHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesFabriclike;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

@AutoService(IDataHelper.class)
public class FabricDataHelper implements IDataHelper {
    @Override
    public ResourceKey<Registry<ConfiguredCowType<?, ?>>> getConfiguredCowTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("bovinesandbuttercups/configured_cow_type"));
    }

    @Override
    public Codec<ConfiguredCowType<?, ?>> getConfiguredCowTypeByNameCodec() {
        return BovineRegistriesFabriclike.CONFIGURED_COW_TYPE.byNameCodec();
    }

    @Override
    public ResourceKey<Registry<FlowerType>> getFlowerTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("bovinesandbuttercups/flower_type"));
    }

    @Override
    public Codec<FlowerType> getFlowerTypeByNameCodec() {
        return BovineRegistriesFabriclike.FLOWER_TYPE.byNameCodec();
    }

    @Override
    public ResourceKey<Registry<MushroomType>> getMushroomTypeResourceKey() {
        return ResourceKey.createRegistryKey(BovinesAndButtercups.asResource("bovinesandbuttercups/mushroom_type"));
    }

    @Override
    public Codec<MushroomType> getMushroomTypeByNameCodec() {
        return BovineRegistriesFabriclike.MUSHROOM_TYPE.byNameCodec();
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return BovineRegistriesFabriclike.COW_TYPE.byNameCodec();
    }
}
