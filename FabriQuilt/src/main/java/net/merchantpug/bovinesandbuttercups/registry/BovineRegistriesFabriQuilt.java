package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.util.ClassUtil;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;

public class BovineRegistriesFabriQuilt {
    public static final Registry<CowType<?>> COW_TYPE = FabricRegistryBuilder.createSimple(ClassUtil.<CowType<?>>castClass(CowType.class), BovineRegistryKeys.COW_TYPE_KEY.location()).buildAndRegister();

    public static final Registry<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = BuiltinRegistries.registerSimple(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovineRegistriesFabriQuilt::bootstrap);

    public static Holder<ConfiguredCowType<?, ?>> bootstrap(Registry<ConfiguredCowType<?, ?>> registry) {
        BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovinesAndButtercups.asResource("missing_mooshroom")), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovinesAndButtercups.asResource("missing_moobloom")), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
    }
}
