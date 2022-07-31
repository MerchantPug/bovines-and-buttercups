package com.github.merchantpug.bovinesandbuttercups.block.renderer;

import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.RandomSource;

public class CustomFlowerRenderer implements BlockEntityRenderer<CustomFlowerBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomFlowerRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomFlowerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.getResourceLocation(), "bovines");

        if (blockEntity.getFlowerType() != null && FlowerTypeRegistry.contains(blockEntity.getFlowerType().getResourceLocation())) {
            modelResourceLocation = new ModelResourceLocation(blockEntity.getFlowerType().getFlowerModel(), blockEntity.getFlowerType().getFlowerModelVariant());
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), flowerModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
