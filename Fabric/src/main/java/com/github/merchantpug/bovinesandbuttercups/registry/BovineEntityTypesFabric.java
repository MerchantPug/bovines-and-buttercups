package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class BovineEntityTypesFabric {
    public static final EntityType<FlowerCow> MOOBLOOM = Registry.register(Registry.ENTITY_TYPE, Constants.resourceLocation("moobloom"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, FlowerCow::new).dimensions(EntityDimensions.scalable(0.9F, 1.4F)).trackRangeChunks(10).build());

    public static void init() {

    }
}
