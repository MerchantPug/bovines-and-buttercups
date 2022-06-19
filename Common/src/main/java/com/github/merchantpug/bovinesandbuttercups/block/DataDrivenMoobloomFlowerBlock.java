package com.github.merchantpug.bovinesandbuttercups.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DataDrivenMoobloomFlowerBlock extends MoobloomFlowerBlock implements EntityBlock {
    public DataDrivenMoobloomFlowerBlock(MobEffect suspiciousStewEffect, int effectDuration, Properties settings) {
        super(suspiciousStewEffect, effectDuration, settings);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(BUD, false));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
