package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.merchantpug.bovinesandbuttercups.util.QuaternionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

import java.util.Optional;

public class CustomFlowerItemRendererHelper {
    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/missing_flower"), "inventory");
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (CustomFlowerItem.getFlowerTypeFromTag(stack).isPresent()) {
            Optional<ResourceLocation> modelLocationWithoutVariant = BovineStatesAssociationRegistry.get(BovineRegistryUtil.getFlowerTypeKey(CustomFlowerItem.getFlowerTypeFromTag(stack).get()), BovineBlocks.CUSTOM_FLOWER.get());
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "inventory");
            }
        }

        BakedModel flowerModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        ItemRenderer itemRenderer =  Minecraft.getInstance().getItemRenderer();

        if (flowerModel.getTransforms() != ItemTransforms.NO_TRANSFORMS) {
            BakedModel originalModel = itemRenderer.getModel(stack, level, null, 0);
            ItemTransform type = originalModel.getTransforms().getTransform(transformType);

            boolean bl = transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
            int translationMultiplier = bl ? -1 : 1;
            poseStack.scale(1.0F / type.scale.x(), 1.0F / type.scale.y(), 1.0F / type.scale.z());
            poseStack.mulPose(QuaternionUtil.inverse(new Quaternionf().rotationXYZ(type.rotation.x() * ((float)Math.PI / 180F), bl ? -type.rotation.y()  * ((float)Math.PI / 180F) : type.rotation.y()  * ((float)Math.PI / 180F), bl ? -type.rotation.z() * ((float)Math.PI / 180F) : type.rotation.z() * ((float)Math.PI / 180F))));
            poseStack.translate(-((float) translationMultiplier * type.translation.x()), -type.translation.y(), -type.translation.z());

            flowerModel.getTransforms().getTransform(transformType).apply(bl, poseStack);
        }

        ((ItemRendererAccessor)itemRenderer).bovinesandbuttercups$invokeRenderModelLists(flowerModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}
