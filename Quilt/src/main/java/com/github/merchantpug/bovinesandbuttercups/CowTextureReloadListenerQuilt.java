package com.github.merchantpug.bovinesandbuttercups;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class CowTextureReloadListenerQuilt extends CowTextureReloadListener implements IdentifiableResourceReloader {
    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return Constants.resourceLocation("cow_textures");
    }
}
