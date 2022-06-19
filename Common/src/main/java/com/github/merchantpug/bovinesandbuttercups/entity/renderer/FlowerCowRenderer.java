package com.github.merchantpug.bovinesandbuttercups.entity.renderer;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.feature.FlowerCowFlowerLayer;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.feature.FlowerCowGrassLayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class FlowerCowRenderer extends MobRenderer<FlowerCow, CowModel<FlowerCow>> {

    public FlowerCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER)), 0.7f);
        this.addLayer(new FlowerCowGrassLayer(this));
        this.addLayer(new FlowerCowFlowerLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(FlowerCow entity) {
        ResourceLocation textureLocation = new ResourceLocation(entity.getFlowerCowType().getResourceLocation().getNamespace(), "textures/entity/moobloom/" + entity.getFlowerCowType().getResourceLocation().getPath().toLowerCase(Locale.ROOT) + "_moobloom.png");
        if (Minecraft.getInstance().getResourceManager().getResource(textureLocation).isPresent()) {
            return textureLocation;
        }
        return BovinesAndButtercupsCommon.resourceLocation("textures/entity/moobloom/missing_moobloom.png");
    }}
