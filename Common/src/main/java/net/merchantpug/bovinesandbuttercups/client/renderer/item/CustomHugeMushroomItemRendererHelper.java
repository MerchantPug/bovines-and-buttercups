package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.client.resources.BovineBlockstateTypes;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.util.QuaternionUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;

import java.util.Optional;

public class CustomHugeMushroomItemRendererHelper {

    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemDisplayContext transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("missing_mushroom_block"), "inventory");
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (CustomHugeMushroomItem.getMushroomTypeFromTag(stack).isPresent()) {
            Optional<ResourceLocation> modelLocationWithoutVariant = BovineStatesAssociationRegistry.getItem(BovineRegistryUtil.getMushroomTypeKey(CustomHugeMushroomItem.getMushroomTypeFromTag(stack).get()), BovineBlockstateTypes.MUSHROOM_BLOCK, false);
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "inventory");
            }
        }

        BakedModel mushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        ItemRenderer itemRenderer =  Minecraft.getInstance().getItemRenderer();

        BakedModel originalModel = itemRenderer.getModel(stack, level, null, 0);
        ItemTransform transform = originalModel.getTransforms().getTransform(transformType);

        boolean left = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        int translationMultiplier = left ? -1 : 1;
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(1.0F / transform.scale.x(), 1.0F / transform.scale.y(), 1.0F / transform.scale.z());
        poseStack.mulPose(QuaternionUtil.inverse(new Quaternionf().rotationXYZ(-(transform.rotation.x() * ((float)Math.PI / 180F)), left ? transform.rotation.y()  * ((float)Math.PI / 180F) : -(transform.rotation.y()  * ((float)Math.PI / 180F)), left ? transform.rotation.z() * ((float)Math.PI / 180F) : -(transform.rotation.z() * ((float)Math.PI / 180F)))));
        poseStack.translate(-((float) translationMultiplier * transform.translation.x()), -transform.translation.y(), -transform.translation.z());

        boolean bl = transformType == ItemDisplayContext.GUI && !mushroomModel.usesBlockLight();
        MultiBufferSource.BufferSource source = null;

        if (bl) {
            Lighting.setupForFlatItems();
            source = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, transformType, left, poseStack, source == null ? bufferSource : source, light, overlay, mushroomModel);

        if (bl) {
            source.endBatch();
            Lighting.setupFor3DItems();
        }
    }

}
