package net.merchantpug.bovinesandbuttercups.client.renderer.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class FlowerCowGrassLayer<T extends FlowerCow, M extends CowModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation FLOWER_COW_GRASS_LOCATION = BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png");

    public FlowerCowGrassLayer(RenderLayerParent<T, M> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, FlowerCow entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInvisible()) return;
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;

        if (entity.getFlowerCowType().getConfiguration().isBackGrassTinted()) {
            int biomeColor = BiomeColors.getAverageGrassColor(entity.level, entity.blockPosition());
            r = (biomeColor >> 16 & 0xFF) / 255.0F;
            g = (biomeColor >> 8 & 0xFF) / 255.0F;
            b = (biomeColor & 0xFF) / 255.0f;
        }
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(FLOWER_COW_GRASS_LOCATION)), light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), r, g, b, 1.0F);
    }
}
