package com.github.merchantpug.bovinesandbuttercups.block.renderer;

import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomMushroomBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomType;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomTypeRegistry;
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

public class CustomMushroomRenderer implements BlockEntityRenderer<CustomMushroomBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomMushroomRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomMushroomBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.getModelLocation(), "bovines");

        if (blockEntity.getMushroomType() != null && MushroomTypeRegistry.contains(blockEntity.getMushroomType().getResourceLocation()) && MushroomTypeRegistry.get(blockEntity.getMushroomType().getResourceLocation()).isWithMushroomBlocks()) {
            modelResourceLocation = new ModelResourceLocation(blockEntity.getMushroomType().getModelLocation(), blockEntity.getMushroomType().getModelVariant());
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), mushroomModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
