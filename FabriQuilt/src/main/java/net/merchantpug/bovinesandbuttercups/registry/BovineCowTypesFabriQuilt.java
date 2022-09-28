package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.core.Registry;

public class BovineCowTypesFabriQuilt {
    public static void register() {
        Registry.register(BovineRegistriesFabriQuilt.COW_TYPE, BovinesAndButtercups.asResource("moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
        Registry.register(BovineRegistriesFabriQuilt.COW_TYPE, BovinesAndButtercups.asResource("mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);

        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, BovinesAndButtercups.asResource("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));
    }
}
