package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BovineBlockEntityTypesForge {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);


    public static void init() {
        BovineBlockEntityTypes.CUSTOM_FLOWER = BLOCK_ENTITY_TYPES.register("custom_flower", () -> BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, Constants.resourceLocation("custom_flower").toString()))).get();
    }
}
