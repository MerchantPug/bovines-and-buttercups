package com.github.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class CowTextureReloadListenerFabric extends CowTextureReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return Constants.resourceLocation("cow_textures");
    }
}
