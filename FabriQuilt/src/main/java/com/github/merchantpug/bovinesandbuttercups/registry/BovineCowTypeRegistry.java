package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.core.Registry;

public class BovineCowTypeRegistry {
    public static void register() {
        Registry.register(BovineRegistriesFabriQuilt.COW_TYPE, Constants.resourceLocation("moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
        Registry.register(BovineRegistriesFabriQuilt.COW_TYPE, Constants.resourceLocation("mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);

        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, Constants.resourceLocation("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
        Registry.register(BovineRegistriesFabriQuilt.CONFIGURED_COW_TYPE, Constants.resourceLocation("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));
    }
}
