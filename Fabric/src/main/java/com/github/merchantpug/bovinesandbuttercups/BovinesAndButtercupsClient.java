package com.github.merchantpug.bovinesandbuttercups;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsClientFabriQuilt.init();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CowTextureReloadListenerFabric());
    }
}
