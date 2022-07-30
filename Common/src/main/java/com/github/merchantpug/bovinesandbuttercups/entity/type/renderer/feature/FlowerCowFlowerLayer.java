package com.github.merchantpug.bovinesandbuttercups.entity.type.renderer.feature;

import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class FlowerCowFlowerLayer<T extends FlowerCow> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public FlowerCowFlowerLayer(RenderLayerParent<T, CowModel<T>> context, BlockRenderDispatcher blockRenderer) {
        super(context);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean bl;
        bl = Minecraft.getInstance().shouldEntityAppearGlowing(livingEntity) && livingEntity.isInvisible();
        if ((livingEntity).isInvisible() && !bl) return;

        BlockState blockState = livingEntity.getFlowerCowType().getFlower();
        ModelResourceLocation modelResourceLocation = null;
        int m = LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f);

        if (livingEntity.isBaby()) {
            blockState = livingEntity.getFlowerCowType().getBud();

            if (livingEntity.getFlowerCowType().getBudModel() != null) {
                modelResourceLocation = new ModelResourceLocation(livingEntity.getFlowerCowType().getBudModel(), livingEntity.getFlowerCowType().getBudModelVariant());
            }

            if (livingEntity.getFlowerCowType().getBud() != null || livingEntity.getFlowerCowType().getBudModel() != null) {
                handleMoobudRender(poseStack, buffer, packedLight, blockRenderer, bl, m, blockState, modelResourceLocation);
            }
        } else {
            if (livingEntity.getFlowerCowType().getFlowerModel() != null) {
                modelResourceLocation = new ModelResourceLocation(livingEntity.getFlowerCowType().getFlowerModel(), livingEntity.getFlowerCowType().getFlowerModelVariant());
            }
            if (livingEntity.getFlowerCowType().getFlower() != null || livingEntity.getFlowerCowType().getFlowerModel() != null) {
                handleMoobloomRender(poseStack, buffer, packedLight, blockRenderer, bl, m, blockState, modelResourceLocation);
            }
        }
    }

    private void handleMoobudRender(PoseStack poseStack, MultiBufferSource buffer, int i, BlockRenderDispatcher blockRenderDispatcher, boolean outlineAndInvisible, int overlay, @Nullable BlockState blockState, @Nullable ModelResourceLocation modelResourceLocation) {
        poseStack.pushPose();
        poseStack.translate(0.2f, 0.35f, 0.5f);
        poseStack.translate(0.1f, 0.0f, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.5f, -0.5f, 0.5f);
        poseStack.translate(-1.0f, -1.0f, -1.0f);
        poseStack.translate(1.5f, 0.075f, 0.4f);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, 0.35f, 0.5f);
        poseStack.translate(0.1f, 0.0f, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.5f, -0.5f, 0.5f);
        poseStack.translate(-1.0f, -1.0f, -1.0f);
        poseStack.translate(1.0f, 0.075f, 0.3f);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0f, 0.625, 0.25f);
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.1, -0.1, -0.1);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
    }

    private void handleMoobloomRender(PoseStack poseStack, MultiBufferSource buffer, int i, BlockRenderDispatcher blockRenderDispatcher, boolean outlineAndInvisible, int overlay, BlockState blockState, @Nullable ModelResourceLocation modelResourceLocation) {
        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.35, -0.133, -0.35);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.5, -0.195, 0.1);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.6, -0.195, -0.4);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(-0.1f, 0.0, -0.7f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(-0.15, -0.257, 0.0);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.0, -0.16, 0.0);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderDispatcher, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
    }

    private void renderFlowerOrBud(PoseStack poseStack, MultiBufferSource buffer, int light, boolean outlineAndInvisible, BlockRenderDispatcher blockRenderDispatcher, int overlay, BlockState flowerState, ModelResourceLocation resourceLocation) {
        BakedModel flowerModel;
        if (flowerState == null) {
            flowerModel = Minecraft.getInstance().getModelManager().getModel(resourceLocation);
        } else {
            flowerModel = blockRenderDispatcher.getBlockModel(flowerState);
        }

        if (outlineAndInvisible) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)), null, flowerModel, 0.0f, 0.0f, 0.0f, light, overlay);
        } else {
            if (flowerState != null) {
                blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(ItemBlockRenderTypes.getRenderType(flowerState, false)), flowerState, flowerModel, 1.0F, 1.0F, 1.0F, light, overlay);
            } else {
                blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(Sheets.cutoutBlockSheet()), null, flowerModel, 1.0F, 1.0F, 1.0F, light, overlay);
            }
        }
    }
}