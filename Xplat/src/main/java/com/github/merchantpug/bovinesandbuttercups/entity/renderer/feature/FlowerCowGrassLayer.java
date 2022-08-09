package com.github.merchantpug.bovinesandbuttercups.entity.renderer.feature;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class FlowerCowGrassLayer extends RenderLayer<FlowerCow, CowModel<FlowerCow>> {
    private static final ResourceLocation FLOWER_COW_GRASS_LOCATION = Constants.resourceLocation("textures/entity/moobloom/moobloom_grass.png");

    public FlowerCowGrassLayer(RenderLayerParent<FlowerCow, CowModel<FlowerCow>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, FlowerCow flowerCow, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (flowerCow.isInvisible()) return;
        int biomeColor = BiomeColors.getAverageGrassColor(flowerCow.level, flowerCow.blockPosition());
        float r = (biomeColor >> 16 & 0xFF) / 255.0F;
        float g = (biomeColor >> 8 & 0xFF) / 255.0F;
        float b = (biomeColor & 0xFF) / 255.0f;
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(FLOWER_COW_GRASS_LOCATION)), light, LivingEntityRenderer.getOverlayCoords(flowerCow, 0.0F), r, g, b, 1.0F);
    }
}