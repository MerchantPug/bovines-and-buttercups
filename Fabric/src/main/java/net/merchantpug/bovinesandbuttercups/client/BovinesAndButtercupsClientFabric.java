package net.merchantpug.bovinesandbuttercups.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketsS2C;
import net.minecraft.server.packs.PackType;

public class BovinesAndButtercupsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsClientFabriclike.init();
        BovinePacketsS2C.registerS2C();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CowTextureReloadListenerFabric());
    }
}
