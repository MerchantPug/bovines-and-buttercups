package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlock;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BovineBlockEntityTypes {
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITY_TYPES = RegistrationProvider.get(Registry.BLOCK_ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<BlockEntityType<CustomFlowerBlockEntity>> CUSTOM_FLOWER = register("custom_flower", () -> BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Constants.resourceLocation("custom_flower").toString())));

    public static void init() {

    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> blockEntityType) {
        return BLOCK_ENTITY_TYPES.register(name, blockEntityType);
    }
}
