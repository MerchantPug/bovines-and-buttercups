package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BovineBlockEntityTypes {
    private static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITY_TYPES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, BovinesAndButtercups.MOD_ID);


    public static final RegistryObject<BlockEntityType<CustomFlowerBlockEntity>> CUSTOM_FLOWER = register("custom_flower", Services.REGISTRY.createCustomFlowerBlockEntity());
    public static final RegistryObject<BlockEntityType<CustomMushroomBlockEntity>> CUSTOM_MUSHROOM = register("custom_mushroom", Services.REGISTRY.createCustomMushroomBlockEntity());
    public static final RegistryObject<BlockEntityType<CustomFlowerPotBlockEntity>> POTTED_CUSTOM_FLOWER = register("potted_custom_flower", Services.REGISTRY.createCustomFlowerPotBlockEntity());
    public static final RegistryObject<BlockEntityType<CustomMushroomPotBlockEntity>> POTTED_CUSTOM_MUSHROOM = register("potted_custom_mushroom", Services.REGISTRY.createCustomMushroomPotBlockEntity());
    public static final RegistryObject<BlockEntityType<CustomHugeMushroomBlockEntity>> CUSTOM_MUSHROOM_BLOCK = register("custom_mushroom_block", Services.REGISTRY.createCustomHugeMushroomBlockEntity());

    public static void register() {

    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, Supplier<BlockEntityType<T>> type) {
        return BLOCK_ENTITY_TYPES.register(name, type);
    }
}
