package com.github.merchantpug.bovinesandbuttercups.client.renderer.entity;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import com.github.merchantpug.bovinesandbuttercups.util.ConfiguredCowTypeRegistryUtil;
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
        ResourceLocation location = new ResourceLocation(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.getLevel(), entity.getFlowerCowType()).getNamespace(), "textures/entity/moobloom/" + ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.getLevel(), entity.getFlowerCowType()).getPath().toLowerCase(Locale.ROOT) + "_moobloom.png");
        if (BovinesAndButtercupsClient.LOADED_COW_TEXTURES.contains(location)) {
            return location;
        }
        return BovinesAndButtercups.asResource("textures/entity/moobloom/missing_moobloom.png");
    }
}
