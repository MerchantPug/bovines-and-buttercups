package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.levelgen.Heightmap;

public class BovineEntityTypesFabriQuilt {
    public static final EntityType<FlowerCow> MOOBLOOM = Registry.register(Registry.ENTITY_TYPE, Constants.resourceLocation("moobloom"), FabricEntityTypeBuilder.createMob().spawnGroup(MobCategory.CREATURE).entityFactory(FlowerCow::new).spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn).dimensions(EntityDimensions.scalable(0.9F, 1.4F)).defaultAttributes(Cow::createAttributes).trackRangeChunks(10).build());

    public static void init() {

    }
}
