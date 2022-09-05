package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.api.CowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.OptionalLong;

public class BovineRegistriesFabriQuilt {
    public static final Registry<CowType<?>> COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), Constants.resourceLocation("cow_type")).buildAndRegister();

    public static final Registry<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovineRegistriesFabriQuilt::bootstrap);

    public static Holder<ConfiguredCowType<?, ?>> bootstrap(Registry<ConfiguredCowType<?, ?>> registry) {
        BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, Constants.resourceLocation("missing_mooshroom")), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, Constants.resourceLocation("missing_moobloom")), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
    }
}
