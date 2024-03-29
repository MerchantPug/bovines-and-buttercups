package net.merchantpug.bovinesandbuttercups.client.renderer.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.merchantpug.bovinesandbuttercups.client.model.FlowerCowModel;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class FlowerCowRenderer extends MobRenderer<FlowerCow, FlowerCowModel<FlowerCow>> {

    public FlowerCowRenderer(EntityRendererProvider.Context context) {
        super(context, new FlowerCowModel<>(context.bakeLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER)), 0.7f);
        this.addLayer(new FlowerCowGrassLayer<>(this));
        this.addLayer(new FlowerCowFlowerLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(FlowerCow entity) {
        ResourceLocation location = entity.getFlowerCowType().configuration().getSettings().cowTexture().orElseGet(() -> {
            ResourceLocation moobloomLocation = BovineRegistryUtil.getConfiguredCowTypeKey(entity.getFlowerCowType()) != null ? BovineRegistryUtil.getConfiguredCowTypeKey(entity.getFlowerCowType()) : BovinesAndButtercups.asResource("missing_moobloom");
            return new ResourceLocation(moobloomLocation.getNamespace().toLowerCase(Locale.ROOT), "textures/entity/bovinesandbuttercups/moobloom/" + moobloomLocation.getPath().toLowerCase(Locale.ROOT) + "_moobloom.png");
        });
        if (BovinesAndButtercupsClient.LOADED_COW_TEXTURES.contains(location)) {
            return location;
        }
        return BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/missing_moobloom.png");
    }
}
