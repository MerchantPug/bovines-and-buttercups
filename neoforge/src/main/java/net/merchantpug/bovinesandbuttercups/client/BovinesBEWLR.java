package net.merchantpug.bovinesandbuttercups.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.CustomFlowerItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.CustomHugeMushroomItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.CustomMushroomItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.client.renderer.item.NectarBowlItemRendererHelper;
import net.merchantpug.bovinesandbuttercups.content.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BovinesBEWLR extends BlockEntityWithoutLevelRenderer {
    public static final BlockEntityWithoutLevelRenderer BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER = new BovinesBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    public BovinesBEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.getItem() instanceof CustomFlowerItem) {
            CustomFlowerItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, transformType);
        } else if (stack.getItem() instanceof CustomMushroomItem) {
            CustomMushroomItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, transformType);
        } else if (stack.getItem() instanceof CustomHugeMushroomItem) {
            CustomHugeMushroomItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, transformType);
        } else if (stack.getItem() instanceof NectarBowlItem) {
            NectarBowlItemRendererHelper.render(stack, poseStack, bufferSource, light, overlay, transformType);
        }
    }
}
