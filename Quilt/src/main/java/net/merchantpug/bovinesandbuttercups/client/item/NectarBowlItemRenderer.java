package net.merchantpug.bovinesandbuttercups.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.NectarBowlItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class NectarBowlItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public NectarBowlItemRenderer() {
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof NectarBowlItem)) return;
        NectarBowlItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, mode);
    }
}
