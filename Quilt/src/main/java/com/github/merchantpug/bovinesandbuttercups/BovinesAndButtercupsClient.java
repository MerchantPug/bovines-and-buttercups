package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.entity.renderer.FlowerCowRenderer;
import com.github.merchantpug.bovinesandbuttercups.networking.BovinePacketsS2C;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.CowModel;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BovinesAndButtercupsCommonClient.init();

        BovinePacketsS2C.register();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);

        EntityRendererRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);
    }
}
