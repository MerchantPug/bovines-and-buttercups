package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BovineBlockEntityTypesForge {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<BlockEntityType<CustomFlowerBlockEntity>> CUSTOM_FLOWER = BLOCK_ENTITY_TYPES.register("custom_flower", () -> BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_flower").toString())));
    public static final RegistryObject<BlockEntityType<CustomMushroomBlockEntity>> CUSTOM_MUSHROOM = BLOCK_ENTITY_TYPES.register("custom_mushroom", () -> BlockEntityType.Builder.of(CustomMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom").toString())));
    public static final RegistryObject<BlockEntityType<CustomFlowerPotBlockEntity>> POTTED_CUSTOM_FLOWER = BLOCK_ENTITY_TYPES.register("potted_custom_flower", () -> BlockEntityType.Builder.of(CustomFlowerPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_flower").toString())));
    public static final RegistryObject<BlockEntityType<CustomMushroomPotBlockEntity>> POTTED_CUSTOM_MUSHROOM = BLOCK_ENTITY_TYPES.register("potted_custom_mushroom", () -> BlockEntityType.Builder.of(CustomMushroomPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_mushroom").toString())));
    public static final RegistryObject<BlockEntityType<CustomHugeMushroomBlockEntity>> CUSTOM_MUSHROOM_BLOCK = BLOCK_ENTITY_TYPES.register("custom_mushroom_block", () -> BlockEntityType.Builder.of(CustomHugeMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom_block").toString())));

    public static void init() {
        BLOCK_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
