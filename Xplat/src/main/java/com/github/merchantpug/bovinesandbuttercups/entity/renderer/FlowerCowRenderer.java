package com.github.merchantpug.bovinesandbuttercups.entity.renderer;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsXplatClient;
import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.feature.FlowerCowFlowerLayer;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.feature.FlowerCowGrassLayer;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class FlowerCowRenderer extends MobRenderer<FlowerCow, CowModel<FlowerCow>> {

    public FlowerCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER)), 0.7f);
        this.addLayer(new FlowerCowGrassLayer(this));
        this.addLayer(new FlowerCowFlowerLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public ResourceLocation getTextureLocation(FlowerCow entity) {
        ResourceLocation location = new ResourceLocation(entity.getFlowerCowType().getId().getNamespace(), "textures/entity/moobloom/" + entity.getFlowerCowType().getId().getPath().toLowerCase(Locale.ROOT) + "_moobloom.png");
        if (BovinesAndButtercupsXplatClient.LOADED_COW_TEXTURES.contains(location)) {
            return location;
        }
        return Constants.resourceLocation("textures/entity/moobloom/missing_moobloom.png");
    }
}
