package com.github.merchantpug.bovinesandbuttercups.client.rendering;

import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItemForge;
import com.github.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class BovinesBEWLR extends BlockEntityWithoutLevelRenderer {
    public static final BlockEntityWithoutLevelRenderer BLOCK_ENTITY_WITHOUT_LEVEL_RENDERER = new BovinesBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());

    public BovinesBEWLR(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
    }

    public void renderByItem(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.getItem() instanceof CustomFlowerItemForge) {
            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(FlowerType.MISSING.getFlowerModel().getNamespace(), FlowerType.MISSING.getFlowerModel().getPath() + "_item"), "bovines");

            if (CustomFlowerItem.getFlowerItemFromTag(stack) != null && FlowerTypeRegistry.contains(CustomFlowerItem.getFlowerItemFromTag(stack).getResourceLocation())) {
                modelResourceLocation = new ModelResourceLocation(new ResourceLocation(CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModel().getNamespace(), CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModel().getPath() + "_item"), CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModelVariant());
            }

            BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

            ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
        }
    }
}
