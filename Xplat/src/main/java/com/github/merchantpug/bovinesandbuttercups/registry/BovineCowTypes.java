package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flower.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.mushroom.MushroomCowType;

public class BovineCowTypes {
    public static final FlowerCowType FLOWER_COW_TYPE = new FlowerCowType();
    public static final MushroomCowType MUSHROOM_COW_TYPE = new MushroomCowType();

    public static void register() {
        CowTypeRegistry.registerType(FLOWER_COW_TYPE);
        CowTypeRegistry.registerType(MUSHROOM_COW_TYPE);
    }
}
