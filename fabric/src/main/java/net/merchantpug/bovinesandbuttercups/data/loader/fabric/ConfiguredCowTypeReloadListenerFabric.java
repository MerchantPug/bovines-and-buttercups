package net.merchantpug.bovinesandbuttercups.data.loader.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.ConfiguredCowTypeReloadListener;
import net.minecraft.resources.ResourceLocation;

public class ConfiguredCowTypeReloadListenerFabric extends ConfiguredCowTypeReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/configured_cow_type");
    }
}
