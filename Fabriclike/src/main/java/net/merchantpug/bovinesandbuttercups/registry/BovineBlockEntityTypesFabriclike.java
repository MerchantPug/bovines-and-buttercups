package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.merchantpug.bovinesandbuttercups.block.entity.*;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BovineBlockEntityTypesFabriclike {
    public static final BlockEntityType<CustomFlowerBlockEntity> CUSTOM_FLOWER = Registry.register(Registry.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_flower"), FabricBlockEntityTypeBuilder.create(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(null));
    public static final BlockEntityType<CustomMushroomBlockEntity> CUSTOM_MUSHROOM = Registry.register(Registry.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom"), FabricBlockEntityTypeBuilder.create(CustomMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM.get()).build(null));
    public static final BlockEntityType<CustomFlowerPotBlockEntity> POTTED_CUSTOM_FLOWER = Registry.register(Registry.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_flower"), FabricBlockEntityTypeBuilder.create(CustomFlowerPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_FLOWER.get()).build(null));
    public static final BlockEntityType<CustomMushroomPotBlockEntity> POTTED_CUSTOM_MUSHROOM = Registry.register(Registry.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("potted_custom_mushroom"), FabricBlockEntityTypeBuilder.create(CustomMushroomPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()).build(null));
    public static final BlockEntityType<CustomHugeMushroomBlockEntity> CUSTOM_MUSHROOM_BLOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE, BovinesAndButtercups.asResource("custom_mushroom_block"), FabricBlockEntityTypeBuilder.create(CustomHugeMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).build(null));

    public static void init() {

    }
}
