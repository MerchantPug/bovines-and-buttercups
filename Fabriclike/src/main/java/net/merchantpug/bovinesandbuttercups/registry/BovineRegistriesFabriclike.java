package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;

public class BovineRegistriesFabriclike {
    public static final Registry<CowType<?>> COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();

    public static final Registry<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, ConfiguredCowType::bootstrap);
    public static final Registry<FlowerType> FLOWER_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.FLOWER_TYPE_KEY, FlowerType::bootstrap);
    public static final Registry<MushroomType> MUSHROOM_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.MUSHROOM_TYPE_KEY, MushroomType::bootstrap);

    public static void register() {
        Registry.register(BovineRegistriesFabriclike.COW_TYPE, BovinesAndButtercups.asResource("moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
        Registry.register(BovineRegistriesFabriclike.COW_TYPE, BovinesAndButtercups.asResource("mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);

        Registry.register(BovineRegistriesFabriclike.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
        Registry.register(BovineRegistriesFabriclike.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));

        Registry.register(BovineRegistriesFabriclike.FLOWER_TYPE, BovinesAndButtercups.asResource("missing_flower"), FlowerType.MISSING);
        Registry.register(BovineRegistriesFabriclike.MUSHROOM_TYPE, BovinesAndButtercups.asResource("missing_mushroom"), MushroomType.MISSING);
    }
}
