package net.merchantpug.bovinesandbuttercups.client.renderer.block;

import net.merchantpug.bovinesandbuttercups.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
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
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.flowerModel().location(), FlowerType.MISSING.flowerModel().variant());

        if (blockEntity.getFlowerType() != null) {
            modelResourceLocation = new ModelResourceLocation(blockEntity.getFlowerType().flowerModel().location(), blockEntity.getFlowerType().flowerModel().variant());
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), flowerModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
