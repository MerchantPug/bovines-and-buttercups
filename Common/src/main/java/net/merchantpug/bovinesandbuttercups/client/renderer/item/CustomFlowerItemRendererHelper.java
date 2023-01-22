package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class CustomFlowerItemRendererHelper {
    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/missing_flower"), "inventory");

        if (CustomFlowerItem.getFlowerTypeFromTag(stack).isPresent()) {
            Optional<ResourceLocation> modelLocationWithoutVariant = BovineStatesAssociationRegistry.get(BovineRegistryUtil.getFlowerTypeKey(CustomFlowerItem.getFlowerTypeFromTag(stack).get()), BovineBlocks.CUSTOM_FLOWER.get());
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "inventory");
            }
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        ItemRenderer itemRenderer =  Minecraft.getInstance().getItemRenderer();
        ((ItemRendererAccessor)itemRenderer).bovinesandbuttercups$invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
