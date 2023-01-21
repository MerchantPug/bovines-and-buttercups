package net.merchantpug.bovinesandbuttercups.data.loader.fabric;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.MushroomTypeReloadListener;
import net.minecraft.resources.ResourceLocation;

public class MushroomTypeReloadListenerFabric extends MushroomTypeReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/mushroom_type");
    }
}
