package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.client.renderer.CowTextureReloadListenerFabric;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import com.github.merchantpug.bovinesandbuttercups.client.renderer.FlowerCowRenderer;
import com.github.merchantpug.bovinesandbuttercups.networking.BovinePacketsS2C;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.model.CowModel;
import net.minecraft.server.packs.PackType;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsCommonClient.init();

        BovinePacketsS2C.register();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);

        EntityRendererRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CowTextureReloadListenerFabric());
    }
}
