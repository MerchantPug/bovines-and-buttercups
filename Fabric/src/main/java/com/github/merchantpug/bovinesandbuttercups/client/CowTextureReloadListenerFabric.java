package com.github.merchantpug.bovinesandbuttercups.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class CowTextureReloadListenerFabric extends CowTextureReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercups.asResource("cow_textures");
    }
}
