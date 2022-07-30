package com.github.merchantpug.bovinesandbuttercups.item;

import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CustomFlowerItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {
    public CustomFlowerItemRenderer() {
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(ItemStack stack, ItemTransforms.TransformType mode, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        if (!(stack.getItem() instanceof CustomFlowerItem)) return;
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(FlowerType.MISSING.getFlowerModel().getNamespace(), FlowerType.MISSING.getFlowerModel().getPath() + "_item"), "bovines");

        if (CustomFlowerItem.getFlowerItemFromTag(stack) != null && FlowerTypeRegistry.contains(CustomFlowerItem.getFlowerItemFromTag(stack).getResourceLocation())) {
            modelResourceLocation = new ModelResourceLocation(new ResourceLocation(CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModel().getNamespace(), CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModel().getPath() + "_item"), CustomFlowerItem.getFlowerItemFromTag(stack).getFlowerModelVariant());
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
