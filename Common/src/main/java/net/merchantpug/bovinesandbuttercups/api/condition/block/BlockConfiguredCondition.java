package net.merchantpug.bovinesandbuttercups.api.condition.block;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockConfiguredCondition<CC extends ConditionConfiguration<BlockInWorld>, CT extends ConditionType<BlockInWorld, CC>> extends ConfiguredCondition<BlockInWorld, CC, CT> {
    public static final Codec<ConfiguredCondition<BlockInWorld, ?, ?>> CODEC = BlockConditionType.CODEC.dispatch(BlockConfiguredCondition::getType, BlockConditionType::getCodec);

    public BlockConfiguredCondition(CT conditionType, CC conditionConfiguration) {
        super(conditionType, conditionConfiguration);
    }
}
