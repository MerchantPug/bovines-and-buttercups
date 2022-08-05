package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BovineBlockEntityTypesFabric {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = Registry.register(Registry.BLOCK_ENTITY_TYPE, Constants.resourceLocation("custom_flower"), FabricBlockEntityTypeBuilder.create(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(null));

    public static void init() {

    }
}
