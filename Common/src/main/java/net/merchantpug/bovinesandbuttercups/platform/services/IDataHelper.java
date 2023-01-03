package net.merchantpug.bovinesandbuttercups.platform.services;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface IDataHelper {

    ResourceKey<Registry<ConfiguredCowType<?, ?>>> getConfiguredCowTypeResourceKey();

    Codec<ConfiguredCowType<?, ?>> getConfiguredCowTypeByNameCodec();

    ResourceKey<Registry<FlowerType>> getFlowerTypeResourceKey();

    Codec<FlowerType> getFlowerTypeByNameCodec();

    ResourceKey<Registry<MushroomType>> getMushroomTypeResourceKey();

    Codec<MushroomType> getMushroomTypeByNameCodec();

    Codec<CowType<?>> getCowTypeCodec();
}
