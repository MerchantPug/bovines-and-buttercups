package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;

public class BovineRegistriesFabriQuilt {
    public static final Registry<CowType<?>> COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();

    public static final Registry<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, ConfiguredCowType::bootstrap);
    public static final Registry<FlowerType> FLOWER_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.FLOWER_TYPE_KEY, FlowerType::bootstrap);
    public static final Registry<MushroomType> MUSHROOM_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.MUSHROOM_TYPE_KEY, MushroomType::bootstrap);
}
