package net.merchantpug.bovinesandbuttercups.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.mixin.client.ItemRendererAccessor;
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

import java.util.Optional;

public class NectarBowlItemRendererHelper {
    public static void render(ItemStack stack, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay, ItemTransforms.TransformType transformType) {
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

        if (nectarBowlModel.getTransforms() != ItemTransforms.NO_TRANSFORMS) {
            BakedModel originalModel = itemRenderer.getModel(stack, level, null, 0);
            ItemTransform type = originalModel.getTransforms().getTransform(transformType);

            boolean bl = transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
            int translationMultiplier = bl ? -1 : 1;
            poseStack.scale(1.0F / type.scale.x(), 1.0F / type.scale.y(), 1.0F / type.scale.z());
            poseStack.mulPose(QuaternionUtil.inverse(new Quaternion(type.rotation.x(), bl ? -type.rotation.y() : type.rotation.y(), bl ? -type.rotation.z() : type.rotation.z(), true)));
            poseStack.translate(-((float) translationMultiplier * type.translation.x()), -type.translation.y(), -type.translation.z());

            nectarBowlModel.getTransforms().getTransform(transformType).apply(bl, poseStack);
        }

        ((ItemRendererAccessor)itemRenderer).bovinesandbuttercups$invokeRenderModelLists(nectarBowlModel, stack, light, overlay, poseStack, ItemRenderer.getFoilBuffer(bufferSource, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil()));
    }
}