package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.core.Registry;

public class BovineRegistryContentFabriQuilt {
    public static void register() {
        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE.get(), FlowerCowConfiguration.MISSING));
        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE.get(), MushroomCowConfiguration.MISSING));

        Registry.register(BovineRegistriesFabriQuilt.FLOWER_TYPE, BovinesAndButtercups.asResource("missing"), FlowerType.MISSING);
        Registry.register(BovineRegistriesFabriQuilt.MUSHROOM_TYPE, BovinesAndButtercups.asResource("missing"), MushroomType.MISSING);
    }
}
