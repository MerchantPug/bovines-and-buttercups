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
    public static final RegistryObject<BlockConditionType<BlockLocationCondition>> BLOCK_LOCATION = register("block_location", () -> new BlockConditionType<>(BlockLocationCondition.CODEC));
    public static final RegistryObject<BlockConditionType<BlockStateCondition>> BLOCK_STATE = register("block_state", () -> new BlockConditionType<>(BlockStateCondition.CODEC));
    public static final RegistryObject<BlockConditionType<CustomFlowerTypeCondition>> CUSTOM_FLOWER_TYPE = register("custom_flower_type", () -> new BlockConditionType<>(CustomFlowerTypeCondition.CODEC));
    public static final RegistryObject<BlockConditionType<CustomMushroomBlockTypeCondition>> CUSTOM_MUSHROOM_BLOCK_TYPE = register("custom_mushroom_block_type", () -> new BlockConditionType<>(CustomMushroomBlockTypeCondition.CODEC));
    public static final RegistryObject<BlockConditionType<CustomMushroomTypeCondition>> CUSTOM_MUSHROOM_TYPE = register("custom_mushroom_type", () -> new BlockConditionType<>(CustomMushroomTypeCondition.CODEC));
    public static final RegistryObject<BlockConditionType<CustomPottedFlowerTypeCondition>> CUSTOM_POTTED_FLOWER_TYPE = register("custom_potted_flower_type", () -> new BlockConditionType<>(CustomPottedFlowerTypeCondition.CODEC));
    public static final RegistryObject<BlockConditionType<CustomPottedMushroomBlockTypeCondition>> CUSTOM_POTTED_MUSHROOM_TYPE = register("custom_potted_mushroom_type", () -> new BlockConditionType<>(CustomPottedMushroomBlockTypeCondition.CODEC));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<BlockInWorld>> RegistryObject<BlockConditionType<CC>> register(String name, Supplier<BlockConditionType<CC>> conditionType) {
        return BLOCK_CONDITION_TYPE.register(name, conditionType);
    }
}
