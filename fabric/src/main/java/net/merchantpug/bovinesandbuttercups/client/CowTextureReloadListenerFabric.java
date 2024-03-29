package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.merchantpug.bovinesandbuttercups.client.util.CowTextureReloadListener;
import net.minecraft.resources.ResourceLocation;

public class CowTextureReloadListenerFabric extends CowTextureReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercups.asResource("cow_textures");
    }
}
