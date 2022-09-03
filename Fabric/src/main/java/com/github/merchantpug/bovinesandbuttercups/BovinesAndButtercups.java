package com.github.merchantpug.bovinesandbuttercups;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;

public class BovinesAndButtercups implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
            String version = modContainer.getMetadata().getVersion().getFriendlyString();
            if (version.contains("+")) {
                version = version.split("\\+")[0];
            }
            if (version.contains("-")) {
                version = version.split("-")[0];
            }
            BovinesAndButtercupsXplat.VERSION = version;
        });
        BovinesAndButtercupsFabriQuilt.init();

        FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(Constants.resourceLocation("mojang"), modContainer, "Mojang Textures", ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(Constants.resourceLocation("no_grass"), modContainer, "No Grass Back", ResourcePackActivationType.NORMAL);
        });
    }
}
