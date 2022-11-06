package net.merchantpug.bovinesandbuttercups.client.item;

import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class CustomFlowerItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public CustomFlowerItemRenderer() {
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof CustomFlowerItem)) return;
        CustomFlowerItem.render(stack, poseStack, bufferSource, light, overlay, mode);
    }
}
