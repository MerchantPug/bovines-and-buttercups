package net.merchantpug.bovinesandbuttercups.data.loader.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.FlowerTypeReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FlowerTypeReloadListenerFabric extends FlowerTypeReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/flower_type");
    }
}