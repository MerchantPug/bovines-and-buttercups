package net.merchantpug.bovinesandbuttercups.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.CustomHugeMushroomItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CustomHugeMushroomItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public CustomHugeMushroomItemRenderer() {
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof CustomHugeMushroomItem)) return;
        CustomHugeMushroomItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, mode);
    }
}
