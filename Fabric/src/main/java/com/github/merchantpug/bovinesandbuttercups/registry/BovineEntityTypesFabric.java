package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class BovineEntityTypesFabric {
    public static final EntityType<FlowerCow> MOOBLOOM = Registry.register(Registry.ENTITY_TYPE, Constants.resourceLocation("moobloom"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, FlowerCow::new).build());

    public static void init() {

    }
}
