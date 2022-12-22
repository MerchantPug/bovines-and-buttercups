package net.merchantpug.bovinesandbuttercups.content.block;

import net.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class SnowdropFlowerBlock extends MoobloomFlowerBlock {
    public SnowdropFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties properties) {
        super(suspiciousStewEffect, effectDuration, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(BovineTags.SNOWDROP_PLACEABLE);
    }
}
