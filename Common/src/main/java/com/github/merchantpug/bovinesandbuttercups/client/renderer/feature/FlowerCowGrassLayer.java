package com.github.merchantpug.bovinesandbuttercups.client.renderer.feature;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class FlowerCowGrassLayer extends RenderLayer<FlowerCow, CowModel<FlowerCow>> {
    private static final ResourceLocation FLOWER_COW_GRASS_LOCATION = BovinesAndButtercupsCommon.resourceLocation("textures/entity/moobloom/moobloom_grass.png");

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
        renderColoredCutoutModel(this.getParentModel(), FLOWER_COW_GRASS_LOCATION, poseStack, buffer, light, flowerCow, r, g, b);
    }
}
