package net.merchantpug.bovinesandbuttercups.data.condition.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class BlockStateConditionConfiguration extends ConditionConfiguration<BlockInWorld> {
    public static final MapCodec<BlockStateConditionConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            BlockState.CODEC.fieldOf("state").forGetter(BlockStateConditionConfiguration::getState)
    ).apply(builder, BlockStateConditionConfiguration::new));

    private final BlockState state;

    public BlockStateConditionConfiguration(BlockState state) {
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
