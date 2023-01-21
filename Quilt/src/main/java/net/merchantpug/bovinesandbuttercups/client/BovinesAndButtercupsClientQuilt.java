package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.network.BovinePacketsS2C;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BovinesAndButtercupsClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BovinesAndButtercupsClientFabriclike.init();
        BovinePacketsS2C.registerS2C();

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new CowTextureReloadListenerQuilt());
    }
}
