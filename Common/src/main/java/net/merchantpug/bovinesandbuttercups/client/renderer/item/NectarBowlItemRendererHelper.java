package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
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

public class NectarBowlItemRendererHelper {
    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemDisplayContext transformType) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/item/colored_nectar_bowl"), "inventory");
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (NectarBowlItem.getCowTypeFromStack(stack) != null) {
            Optional<ResourceLocation> modelLocationWithoutVariant = NectarBowlItem.getCowTypeFromStack(stack).getConfiguration().getNectarTexture();
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "inventory");
            }
        } else {
            modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/item/buttercup_nectar_bowl"), "inventory");
        }

        BakedModel nectarBowlModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);
        ItemRenderer itemRenderer =  Minecraft.getInstance().getItemRenderer();

        BakedModel originalModel = itemRenderer.getModel(stack, level, null, 0);
        ItemTransform type = originalModel.getTransforms().getTransform(transformType);

        boolean left = transformType == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || transformType == ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        int translationMultiplier = left ? -1 : 1;
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(1.0F / type.scale.x(), 1.0F / type.scale.y(), 1.0F / type.scale.z());
        poseStack.mulPose(QuaternionUtil.inverse(new Quaternionf().rotationXYZ(type.rotation.x() * ((float)Math.PI / 180F), left ? -type.rotation.y()  * ((float)Math.PI / 180F) : type.rotation.y()  * ((float)Math.PI / 180F), left ? -type.rotation.z() * ((float)Math.PI / 180F) : type.rotation.z() * ((float)Math.PI / 180F))));
        poseStack.translate(-((float) translationMultiplier * type.translation.x()), -type.translation.y(), -type.translation.z());

        boolean bl = transformType == ItemDisplayContext.GUI && !nectarBowlModel.usesBlockLight();
        MultiBufferSource.BufferSource source = null;

        if (bl) {
            Lighting.setupForFlatItems();
            source = Minecraft.getInstance().renderBuffers().bufferSource();
        }

        Minecraft.getInstance().getItemRenderer().render(stack, transformType, left, poseStack, source == null ? bufferSource : source, light, overlay, nectarBowlModel);

        if (bl) {
            source.endBatch();
            Lighting.setupFor3DItems();
        }
    }
}
