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
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Supplier;

public class BovineBlockConditionTypes {
    public static final RegistrationProvider<BlockConditionType<?>> BLOCK_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final Supplier<BlockConditionType<AndConditionConfiguration<BlockInWorld>>> AND = register("and", () -> new BlockConditionType<>(AndConditionConfiguration.getCodec(BlockConfiguredCondition.getCodec())));
    public static final Supplier<BlockConditionType<ConstantConditionConfiguration<BlockInWorld>>> CONSTANT = register("constant", () -> new BlockConditionType<>(ConstantConditionConfiguration.getCodec()));
    public static final Supplier<BlockConditionType<NotConditionConfiguration<BlockInWorld>>> NOT = register("not", () -> new BlockConditionType<>(NotConditionConfiguration.getCodec(BlockConfiguredCondition.getCodec())));
    public static final Supplier<BlockConditionType<OrConditionConfiguration<BlockInWorld>>> OR = register("or", () -> new BlockConditionType<>(OrConditionConfiguration.getCodec(BlockConfiguredCondition.getCodec())));
    public static final Supplier<BlockConditionType<BlockLocationCondition>> BLOCK_LOCATION = register("block_location", () -> new BlockConditionType<>(BlockLocationCondition.getCodec()));
    public static final Supplier<BlockConditionType<BlockStateCondition>> BLOCK_STATE = register("block_state", () -> new BlockConditionType<>(BlockStateCondition.getCodec()));
    public static final Supplier<BlockConditionType<CustomFlowerTypeCondition>> CUSTOM_FLOWER_TYPE = register("custom_flower_type", () -> new BlockConditionType<>(CustomFlowerTypeCondition.getCodec()));
    public static final Supplier<BlockConditionType<CustomMushroomBlockTypeCondition>> CUSTOM_MUSHROOM_BLOCK_TYPE = register("custom_mushroom_block_type", () -> new BlockConditionType<>(CustomMushroomBlockTypeCondition.getCodec()));
    public static final Supplier<BlockConditionType<CustomMushroomTypeCondition>> CUSTOM_MUSHROOM_TYPE = register("custom_mushroom_type", () -> new BlockConditionType<>(CustomMushroomTypeCondition.getCodec()));
    public static final Supplier<BlockConditionType<CustomPottedFlowerTypeCondition>> CUSTOM_POTTED_FLOWER_TYPE = register("custom_potted_flower_type", () -> new BlockConditionType<>(CustomPottedFlowerTypeCondition.getCodec()));
    public static final Supplier<BlockConditionType<CustomPottedMushroomBlockTypeCondition>> CUSTOM_POTTED_MUSHROOM_TYPE = register("custom_potted_mushroom_type", () -> new BlockConditionType<>(CustomPottedMushroomBlockTypeCondition.getCodec()));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<BlockInWorld>> Supplier<BlockConditionType<CC>> register(String name, Supplier<BlockConditionType<CC>> conditionType) {
        return BLOCK_CONDITION_TYPE.register(name, conditionType);
    }
}
