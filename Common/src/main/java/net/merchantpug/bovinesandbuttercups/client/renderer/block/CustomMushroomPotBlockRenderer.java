package net.merchantpug.bovinesandbuttercups.client.renderer.block;

import net.merchantpug.bovinesandbuttercups.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
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

public class CustomMushroomPotBlockRenderer implements BlockEntityRenderer<CustomMushroomPotBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomMushroomPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomMushroomPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.pottedModelLocation(), MushroomType.MISSING.pottedModelVariant());

        if (blockEntity.getMushroomType() != null) {
            modelResourceLocation = new ModelResourceLocation(blockEntity.getMushroomType().pottedModelLocation(), blockEntity.getMushroomType().pottedModelVariant());
        }

        BakedModel pottedMushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), pottedMushroomModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}