package net.merchantpug.bovinesandbuttercups.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItemForge;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;

public class BovinesBEWLR extends BlockEntityWithoutLevelRenderer {
    public static final BlockEntityWithoutLevelRenderer BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER = new BovinesBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    public BovinesBEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.getItem() instanceof CustomFlowerItem) {
            CustomFlowerItem.render(stack, poseStack, bufferSource, light, overlay, transformType);
        } else if (stack.getItem() instanceof CustomMushroomItem) {
            CustomMushroomItem.render(stack, poseStack, bufferSource, light, overlay, transformType);
        } else if (stack.getItem() instanceof CustomHugeMushroomItemForge) {
            CustomHugeMushroomItem.render(stack, poseStack, bufferSource, light, overlay, transformType);
        }
    }
}
