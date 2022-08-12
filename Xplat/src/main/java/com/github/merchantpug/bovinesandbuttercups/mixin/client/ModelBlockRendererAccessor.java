package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;
import java.util.BitSet;

@Mixin(ModelBlockRenderer.class)
public interface ModelBlockRendererAccessor {
    @Invoker("calculateShape")
    void bovinesandbuttercups$invokeCalculateShape(BlockAndTintGetter $$0, BlockState $$1, BlockPos $$2, int[] $$3, Direction $$4, @Nullable float[] $$5, BitSet $$6);

    @Invoker("putQuadData")
    void bovinesandbuttercups$invokePutQuadData(BlockAndTintGetter $$0, BlockState $$1, BlockPos $$2, VertexConsumer $$3, PoseStack.Pose $$4, BakedQuad $$5, float $$6, float $$7, float $$8, float $$9, int $$10, int $$11, int $$12, int $$13, int $$14);
}
