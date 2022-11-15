package net.merchantpug.bovinesandbuttercups.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowMyceliumLayer extends RenderLayer<MushroomCow, CowModel<MushroomCow>> {
    public MushroomCowMyceliumLayer(RenderLayerParent<MushroomCow, CowModel<MushroomCow>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, MushroomCow entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.isInvisible()) return;
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;

        if (Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getBackGrassConfiguration().grassTinted()) {
            int biomeColor = BiomeColors.getAverageGrassColor(entity.level, entity.blockPosition());
            r = (biomeColor >> 16 & 0xFF) / 255.0F;
            g = (biomeColor >> 8 & 0xFF) / 255.0F;
            b = (biomeColor & 0xFF) / 255.0f;
        }
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getBackGrassConfiguration().textureLocation())), light, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), r, g, b, 1.0F);
    }
}
