package net.merchantpug.bovinesandbuttercups.registry.condition;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.AndConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.ConstantConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.OrConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.condition.block.BlockLocationCondition;
import net.merchantpug.bovinesandbuttercups.data.condition.block.BlockStateCondition;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.merchantpug.bovinesandbuttercups.registry.RegistrationProvider;
import net.merchantpug.bovinesandbuttercups.registry.RegistryObject;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.function.Supplier;

public class BovineBlockConditionTypes {
    public static final RegistrationProvider<BlockConditionType<?>> BLOCK_CONDITION_TYPE = RegistrationProvider.get(BovineRegistryKeys.BLOCK_CONDITION_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<BlockConditionType<AndConditionConfiguration<BlockInWorld>>> AND = register("and", () -> new BlockConditionType<>(AndConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<ConstantConditionConfiguration<BlockInWorld>>> CONSTANT = register("constant", () -> new BlockConditionType<>(ConstantConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<NotConditionConfiguration<BlockInWorld>>> NOT = register("not", () -> new BlockConditionType<>(NotConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<OrConditionConfiguration<BlockInWorld>>> OR = register("or", () -> new BlockConditionType<>(OrConditionConfiguration.getCodec(BlockConfiguredCondition.CODEC)));
    public static final RegistryObject<BlockConditionType<BlockLocationCondition>> BLOCK_LOCATION = register("block_location", () -> new BlockConditionType<>(BlockLocationCondition.CODEC));
    public static final RegistryObject<BlockConditionType<BlockStateCondition>> BLOCK_STATE = register("block_state", () -> new BlockConditionType<>(BlockStateCondition.CODEC));

    public static void register() {

    }

    private static <CC extends ConditionConfiguration<BlockInWorld>> RegistryObject<BlockConditionType<CC>> register(String name, Supplier<BlockConditionType<CC>> cowType) {
        return BLOCK_CONDITION_TYPE.register(name, cowType);
    }
}
