package net.merchantpug.bovinesandbuttercups.client.renderer.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class FlowerCowFlowerLayer<T extends FlowerCow> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public FlowerCowFlowerLayer(RenderLayerParent<T, CowModel<T>> context, BlockRenderDispatcher blockRenderer) {
        super(context);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean bl;
        bl = Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible();
        if ((entity).isInvisible() && !bl) return;

        FlowerCowConfiguration configuration = entity.getFlowerCowType().getConfiguration();

        int m = LivingEntityRenderer.getOverlayCoords(entity, 0.0f);

        Optional<BlockState> blockState;
        ModelResourceLocation modelResourceLocation;

        if (entity.isBaby()) {
            if (configuration.getBud().modelLocation().isPresent()) {
                modelResourceLocation = new ModelResourceLocation(configuration.getBud().modelLocation().get(), configuration.getBud().modelVariant());
            } else if (configuration.getBud().getFlowerType(entity.getLevel()).isPresent()) {
                modelResourceLocation = new ModelResourceLocation(configuration.getBud().getFlowerType(entity.getLevel()).get().modelLocation(), configuration.getBud().getFlowerType(entity.getLevel()).get().modelVariant());
            } else {
                modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.modelLocation(), FlowerType.MISSING.modelVariant());
            }
            blockState = configuration.getBud().blockState();
        } else {
            if (configuration.getFlower().modelLocation().isPresent()) {
                modelResourceLocation = new ModelResourceLocation(configuration.getFlower().modelLocation().get(), configuration.getFlower().modelVariant());
            } else if (configuration.getFlower().getFlowerType(entity.getLevel()).isPresent()) {
                modelResourceLocation = new ModelResourceLocation(configuration.getFlower().getFlowerType(entity.getLevel()).get().modelLocation(), configuration.getFlower().getFlowerType(entity.getLevel()).get().modelVariant());
            } else {
                modelResourceLocation = new ModelResourceLocation(FlowerType.MISSING.modelLocation(), FlowerType.MISSING.modelVariant());
            }
            blockState = configuration.getFlower().blockState();
        }

        handleMoobloomRender(poseStack, buffer, entity, packedLight, bl, m, blockState, modelResourceLocation);
    }

    private void handleMoobudRender(PoseStack poseStack, MultiBufferSource buffer, T entity, int i, boolean outlineAndInvisible, int overlay, Optional<BlockState> blockState, @Nullable ModelResourceLocation modelResourceLocation) {
        poseStack.pushPose();
        if (entity.getStandingStillForBeeTicks() > 0) {
            poseStack.translate(0.0f, 11.0F / 16.0F, 0.0f);
        }

        poseStack.pushPose();
        poseStack.translate(0.2f, 0.35f, 0.5f);
        poseStack.translate(0.1f, 0.0f, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.5f, -0.5f, 0.5f);
        poseStack.translate(-1.0f, -1.0f, -1.0f);
        poseStack.translate(1.5f, 0.075f, 0.4f);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, 0.35f, 0.5f);
        poseStack.translate(0.1f, 0.0f, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.5f, -0.5f, 0.5f);
        poseStack.translate(-1.0f, -1.0f, -1.0f);
        poseStack.translate(1.0f, 0.075f, 0.3f);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.0f, 0.625, 0.25f);
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.1, -0.1, -0.1);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
    }

    private void handleMoobloomRender(PoseStack poseStack, MultiBufferSource buffer, T entity, int i, boolean outlineAndInvisible, int overlay, Optional<BlockState> blockState, @Nullable ModelResourceLocation modelResourceLocation) {
        poseStack.pushPose();
        if (entity.getStandingStillForBeeTicks() > 0) {
            poseStack.translate(0.0f, 11.0F / 16.0F, 0.0f);
        }

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.35, -0.133, -0.35);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.5, -0.195, 0.1);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(0.1f, 0.0, -0.6f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.6, -0.195, -0.4);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2f, -0.35f, 0.5);
        poseStack.translate(-0.1f, 0.0, -0.7f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(-0.15, -0.257, 0.0);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
        poseStack.popPose();

        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0, -0.7f, -0.2f);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(45.0f));
        poseStack.scale(-0.75f, -0.75f, 0.75f);
        poseStack.translate(-0.5, -0.5, -0.5);
        poseStack.translate(0.0, -0.16, 0.0);
        this.renderFlowerOrBud(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
    }

    private void renderFlowerOrBud(PoseStack poseStack, MultiBufferSource buffer, int light, boolean outlineAndInvisible, BlockRenderDispatcher blockRenderDispatcher, int overlay, Optional<BlockState> flowerState, ModelResourceLocation resourceLocation) {
        BakedModel flowerModel;
        flowerModel = flowerState.map(blockRenderDispatcher::getBlockModel).orElseGet(() -> Minecraft.getInstance().getModelManager().getModel(resourceLocation));

        if (outlineAndInvisible) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)), null, flowerModel, 0.0f, 0.0f, 0.0f, light, overlay);
        } else {
            if (flowerState.isPresent()) {
                blockRenderDispatcher.renderSingleBlock(flowerState.get(), poseStack, buffer, light, overlay);
            } else {
                blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(Sheets.cutoutBlockSheet()), null, flowerModel, 1.0F, 1.0F, 1.0F, light, overlay);
            }
        }
    }
}