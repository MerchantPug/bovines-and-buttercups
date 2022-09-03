package com.github.merchantpug.bovinesandbuttercups;

import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BovinesAndButtercupsClientFabriQuilt.init();
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new CowTextureReloadListenerQuilt());
    }
}
