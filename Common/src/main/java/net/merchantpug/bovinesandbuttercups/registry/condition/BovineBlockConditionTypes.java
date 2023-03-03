package net.merchantpug.bovinesandbuttercups.registry.condition;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.AndConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.ConstantConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.OrConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.condition.block.*;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import net.merchantpug.bovinesandbuttercups.registry.RegistryObject;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Supplier;

public class BovineBlockConditionTypes {
    private static final RegistrationProvider<BlockConditionType<?>> BLOCK_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<BlockConditionType<AndConditionConfiguration<BlockInWorld>>> AND = register("and", () -> new BlockConditionType<>(AndConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<ConstantConditionConfiguration<BlockInWorld>>> CONSTANT = register("constant", () -> new BlockConditionType<>(ConstantConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<NotConditionConfiguration<BlockInWorld>>> NOT = register("not", () -> new BlockConditionType<>(NotConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<OrConditionConfiguration<BlockInWorld>>> OR = register("or", () -> new BlockConditionType<>(OrConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<BlockLocationConditionConfiguration>> BLOCK_LOCATION = register("block_location", () -> new BlockConditionType<>(BlockLocationConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<BlockStateConditionConfiguration>> BLOCK_STATE = register("block_state", () -> new BlockConditionType<>(BlockStateConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<CustomFlowerTypeConditionConfiguration>> CUSTOM_FLOWER_TYPE = register("custom_flower_type", () -> new BlockConditionType<>(CustomFlowerTypeConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<CustomMushroomBlockTypeConditionConfiguration>> CUSTOM_MUSHROOM_BLOCK_TYPE = register("custom_mushroom_block_type", () -> new BlockConditionType<>(CustomMushroomBlockTypeConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<CustomMushroomTypeConditionConfiguration>> CUSTOM_MUSHROOM_TYPE = register("custom_mushroom_type", () -> new BlockConditionType<>(CustomMushroomTypeConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<CustomPottedFlowerTypeConditionConfiguration>> CUSTOM_POTTED_FLOWER_TYPE = register("custom_potted_flower_type", () -> new BlockConditionType<>(CustomPottedFlowerTypeConditionConfiguration.CODEC));
    public static final RegistryObject<BlockConditionType<CustomPottedMushroomBlockTypeConditionConfiguration>> CUSTOM_POTTED_MUSHROOM_TYPE = register("custom_potted_mushroom_type", () -> new BlockConditionType<>(CustomPottedMushroomBlockTypeConditionConfiguration.CODEC));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<BlockInWorld>> RegistryObject<BlockConditionType<CC>> register(String name, Supplier<BlockConditionType<CC>> conditionType) {
        return BLOCK_CONDITION_TYPE.register(name, conditionType);
    }
}
