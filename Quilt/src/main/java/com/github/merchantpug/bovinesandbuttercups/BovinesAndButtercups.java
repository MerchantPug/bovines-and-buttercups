package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.entity.type.CowLoaderQuilt;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;

public class BovinesAndButtercups implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        String version = mod.metadata().version().raw();
        if (version.contains("+")) {
            version = version.split("\\+")[0];
        }
        if (version.contains("-")) {
            version = version.split("-")[0];
        }
        BovinesAndButtercupsXplat.VERSION = version;
        BovinesAndButtercupsFabriQuilt.init();

        ResourceLoader.registerBuiltinResourcePack(Constants.resourceLocation("mojang"), mod, ResourcePackActivationType.NORMAL, Component.literal("Mojang Textures"));
        ResourceLoader.registerBuiltinResourcePack(Constants.resourceLocation("no_grass"), mod, ResourcePackActivationType.NORMAL, Component.literal("No Grass Back"));

        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new CowLoaderQuilt());
    }
}