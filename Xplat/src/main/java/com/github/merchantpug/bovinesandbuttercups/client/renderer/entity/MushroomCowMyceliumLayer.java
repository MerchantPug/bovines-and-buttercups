package com.github.merchantpug.bovinesandbuttercups.client.renderer.entity;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowMyceliumLayer extends RenderLayer<MushroomCow, CowModel<MushroomCow>> {
    private static final ResourceLocation MUSHROOM_COW_MYCELIUM_LOCATION = BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png");

    public MushroomCowMyceliumLayer(RenderLayerParent<MushroomCow, CowModel<MushroomCow>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, MushroomCow mushroomCow, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (mushroomCow.isInvisible()) return;
        this.getParentModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(MUSHROOM_COW_MYCELIUM_LOCATION)), light, LivingEntityRenderer.getOverlayCoords(mushroomCow, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
