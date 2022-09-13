package com.github.merchantpug.bovinesandbuttercups.client.item;

import com.github.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class CustomMushroomItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public CustomMushroomItemRenderer() {
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof CustomMushroomItem)) return;
        CustomMushroomItem.render(stack, poseStack, bufferSource, light, overlay, mode);
    }
}
