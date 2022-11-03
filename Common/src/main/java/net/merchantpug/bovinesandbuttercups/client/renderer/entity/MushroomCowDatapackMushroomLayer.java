package net.merchantpug.bovinesandbuttercups.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
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
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Optional;

public class MushroomCowDatapackMushroomLayer<T extends MushroomCow> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public MushroomCowDatapackMushroomLayer(RenderLayerParent<T, CowModel<T>> context, BlockRenderDispatcher blockRenderer) {
        super(context);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean bl = Minecraft.getInstance().shouldEntityAppearGlowing(entity) && entity.isInvisible();
        if (entity.isInvisible() && !bl
                || entity.isBaby()
                || (BovineRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.COMPONENT.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("red_mushroom"))
                && Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getMushroom().blockState().isPresent() && Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getMushroom().blockState().get().is(Blocks.RED_MUSHROOM))
                || (BovineRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.COMPONENT.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("brown_mushroom"))
                && Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getMushroom().blockState().isPresent() && Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getMushroom().blockState().get().is(Blocks.BROWN_MUSHROOM))) return;

        MushroomCowConfiguration configuration = Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration();

        int m = LivingEntityRenderer.getOverlayCoords(entity, 0.0f);

        ModelResourceLocation modelResourceLocation;
        if (configuration.getMushroom().modelLocation().isPresent()) {
            modelResourceLocation = new ModelResourceLocation(configuration.getMushroom().modelLocation().get(), configuration.getMushroom().modelVariant());
        } else if (configuration.getMushroom().getMushroomType(entity.getLevel()).isPresent()) {
            modelResourceLocation = new ModelResourceLocation(configuration.getMushroom().getMushroomType(entity.getLevel()).get().mushroomModel().location(), configuration.getMushroom().getMushroomType(entity.getLevel()).get().mushroomModel().variant());
        } else {
            modelResourceLocation = new ModelResourceLocation(MushroomType.MISSING.mushroomModel().location(), MushroomType.MISSING.mushroomModel().variant());
        }

        handleMooshroomRender(poseStack, buffer, packedLight, bl, m, configuration.getMushroom().blockState(), modelResourceLocation);
    }

    private void handleMooshroomRender(PoseStack poseStack, MultiBufferSource buffer, int i, boolean outlineAndInvisible, int overlay, Optional<BlockState> blockState, @Nullable ModelResourceLocation modelResourceLocation) {
        poseStack.pushPose();
        poseStack.translate(0.2F, -0.35F, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0.2F, -0.35F, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
        poseStack.translate(0.1F, 0.0D, -0.6F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();

        poseStack.pushPose();
        this.getParentModel().getHead().translateAndRotate(poseStack);
        poseStack.translate(0.0D, -0.7F, -0.2F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(-0.5D, -0.5D, -0.5D);
        this.renderMushroomBlock(poseStack, buffer, i, outlineAndInvisible, blockRenderer, overlay, blockState, modelResourceLocation);
        poseStack.popPose();
    }

    private void renderMushroomBlock(PoseStack poseStack, MultiBufferSource buffer, int light, boolean outlineAndInvisible, BlockRenderDispatcher blockRenderDispatcher, int overlay, Optional<BlockState> mushroomState, ModelResourceLocation resourceLocation) {
        BakedModel mushroomModel = mushroomState.map(blockRenderDispatcher::getBlockModel).orElseGet(() -> Minecraft.getInstance().getModelManager().getModel(resourceLocation));

        if (outlineAndInvisible) {
            blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(RenderType.outline(InventoryMenu.BLOCK_ATLAS)), null, mushroomModel, 0.0f, 0.0f, 0.0f, light, overlay);
        } else {
            if (mushroomState.isPresent()) {
                blockRenderDispatcher.renderSingleBlock(mushroomState.get(), poseStack, buffer, light, overlay);
            } else {
                blockRenderDispatcher.getModelRenderer().renderModel(poseStack.last(), buffer.getBuffer(Sheets.cutoutBlockSheet()), null, mushroomModel, 1.0F, 1.0F, 1.0F, light, overlay);
            }
        }
    }
}
