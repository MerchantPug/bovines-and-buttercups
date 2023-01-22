package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
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
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CustomHugeMushroomItemRendererHelper {

    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/missing_mushroom_block"), "inventory");

        Level level = Minecraft.getInstance().level;
        if (level != null && CustomHugeMushroomItem.getMushroomTypeFromTag(stack).isPresent()) {
            Optional<ResourceLocation> modelLocationWithoutVariant = BovineStatesAssociationRegistry.get(BovineRegistryUtil.getMushroomTypeKey(CustomHugeMushroomItem.getMushroomTypeFromTag(stack).get()), BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get());
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "inventory");
            }
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        ItemRenderer itemRenderer =  Minecraft.getInstance().getItemRenderer();
        ((ItemRendererAccessor)itemRenderer).bovinesandbuttercups$invokeRenderModelLists(mushroomModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
