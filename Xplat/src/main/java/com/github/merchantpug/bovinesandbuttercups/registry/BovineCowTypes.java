package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow.FlowerCowType;

public class BovineCowTypes {
    public static final FlowerCowType FLOWER_COW_TYPE = new FlowerCowType();

    public static void register() {
        CowTypeRegistry.registerType(FLOWER_COW_TYPE);
    }
}
