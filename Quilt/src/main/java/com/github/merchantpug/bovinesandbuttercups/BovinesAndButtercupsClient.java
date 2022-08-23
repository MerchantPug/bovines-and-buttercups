package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.network.BovinePacketsS2C;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BovinesAndButtercupsClientFabriQuilt.init();
        BovinePacketsS2C.register();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new CowTextureReloadListenerQuilt());
    }
}
