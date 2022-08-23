package com.github.merchantpug.bovinesandbuttercups.block.renderer;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomHugeMushroomBlock;
import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomHugeMushroomBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomMushroomBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomType;
import com.github.merchantpug.bovinesandbuttercups.data.block.mushroom.MushroomTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.mixin.client.ModelBlockRendererAccessor;
import com.github.merchantpug.bovinesandbuttercups.mixin.client.ModelBlockRendererAmbientOcclusionFaceAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.BitSet;
import java.util.List;

public class CustomHugeMushroomBlockRenderer implements BlockEntityRenderer<CustomHugeMushroomBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomHugeMushroomBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomHugeMushroomBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.getHugeBlockModelLocation(), "bovines");

        if (blockEntity.getMushroomType() != null && MushroomTypeRegistry.contains(blockEntity.getMushroomType().getResourceLocation()) && MushroomTypeRegistry.get(blockEntity.getMushroomType().getResourceLocation()).isWithMushroomBlocks()) {
            modelResourceLocation = new ModelResourceLocation(blockEntity.getMushroomType().getHugeBlockModelLocation(), blockEntity.getMushroomType().getHugeBlockModelVariant());
        }

        BakedModel mushroomBlockModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        BakedModel mushroomBlockInsideModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation("mushroom_block_inside"), "bovines"));

        boolean northOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.NORTH);
        if (northOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.NORTH);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.NORTH);
        }

        boolean eastOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.EAST);
        poseStack.pushPose();
        poseStack.translate(1.0, 0.0, 0.0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(270.0f));
        if (eastOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.EAST);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.EAST);
        }
        poseStack.popPose();

        boolean southOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.SOUTH);
        poseStack.pushPose();
        poseStack.translate(1.0, 0.0, 1.0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        if (southOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.SOUTH);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.SOUTH);
        }
        poseStack.popPose();

        boolean westOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.WEST);
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 1.0);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0f));
        if (westOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.WEST);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.WEST);
        }
        poseStack.popPose();

        boolean downOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.DOWN);
        poseStack.pushPose();
        poseStack.translate(0.0, 0.0, 1.0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(270.0f));
        if (downOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.DOWN);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.DOWN);
        }
        poseStack.popPose();

        boolean upOutwards = blockEntity.getBlockState().getValue(CustomHugeMushroomBlock.UP);
        poseStack.pushPose();
        poseStack.translate(0.0, 1.0, 0.0);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
        if (upOutwards) {
            handleFaceRenderOuter(mushroomBlockModel, blockEntity, poseStack, bufferSource, Direction.UP);
        } else {
            handleFaceRenderInner(mushroomBlockInsideModel, blockEntity, poseStack, bufferSource, Direction.UP);
        }
        poseStack.popPose();
    }

    private void handleFaceRenderOuter(BakedModel model, CustomHugeMushroomBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, Direction direction) {
        List<BakedQuad> quadList = model.getQuads(blockEntity.getBlockState(), Direction.NORTH, RandomSource.create(blockEntity.getBlockState().getSeed(blockEntity.getBlockPos())));

        BitSet bitSet = new BitSet(3);
        ModelBlockRenderer.AmbientOcclusionFace ambientOcclusionFace = new ModelBlockRenderer.AmbientOcclusionFace();
        float[] floats = new float[LevelRenderer.DIRECTIONS.length * 2];

        for(BakedQuad bakedQuad : quadList) {
            ((ModelBlockRendererAccessor)blockRenderDispatcher.getModelRenderer()).bovinesandbuttercups$invokeCalculateShape(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos(), bakedQuad.getVertices(), Direction.NORTH, floats, bitSet);
            ambientOcclusionFace.calculate(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos(), direction, floats, bitSet, bakedQuad.isShade());
            ((ModelBlockRendererAccessor)blockRenderDispatcher.getModelRenderer()).bovinesandbuttercups$invokePutQuadData(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos(), bufferSource.getBuffer(RenderType.solid()), poseStack.last(), bakedQuad, ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getBrightness()[0], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getBrightness()[1], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getBrightness()[2], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getBrightness()[3], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getLightmap()[0], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getLightmap()[1], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getLightmap()[2], ((ModelBlockRendererAmbientOcclusionFaceAccessor)ambientOcclusionFace).bovinesandbuttercups$getLightmap()[3], OverlayTexture.NO_OVERLAY);
        }
    }

    private void handleFaceRenderInner(BakedModel model,  CustomHugeMushroomBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, Direction direction) {
        List<BakedQuad> quadList = model.getQuads(blockEntity.getBlockState(), Direction.NORTH, RandomSource.create(blockEntity.getBlockState().getSeed(blockEntity.getBlockPos())));

        BitSet bitSet = new BitSet(3);

        for(BakedQuad bakedQuad : quadList) {
            ((ModelBlockRendererAccessor)blockRenderDispatcher.getModelRenderer()).bovinesandbuttercups$invokeCalculateShape(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos(), bakedQuad.getVertices(), Direction.NORTH, (float[])null, bitSet);
            BlockPos blockPos2 = bitSet.get(0) ? blockEntity.getBlockPos().relative(direction) : blockEntity.getBlockPos();
            int packedLight = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockState(), blockPos2);

            float brightness = blockEntity.getLevel().getShade(direction, bakedQuad.isShade());
            ((ModelBlockRendererAccessor)blockRenderDispatcher.getModelRenderer()).bovinesandbuttercups$invokePutQuadData(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos(), bufferSource.getBuffer(RenderType.solid()), poseStack.last(), bakedQuad, brightness, brightness, brightness, brightness, packedLight, packedLight, packedLight, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
