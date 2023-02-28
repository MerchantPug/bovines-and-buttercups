package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockStateCondition extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<BlockStateCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BlockState.CODEC.fieldOf("state").forGetter(BlockStateCondition::getState)
    ).apply(builder, BlockStateCondition::new));

    private final BlockState state;

    public BlockStateCondition(BlockState state) {
        this.state = state;
    }

    @Override
    public boolean test(BlockInWorld block) {
        return block.getState() == state;
    }

    public BlockState getState() {
        return state;
    }
}
