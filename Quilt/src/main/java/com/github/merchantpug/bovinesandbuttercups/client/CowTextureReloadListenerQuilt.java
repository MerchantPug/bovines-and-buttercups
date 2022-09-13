package com.github.merchantpug.bovinesandbuttercups.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class CowTextureReloadListenerQuilt extends CowTextureReloadListener implements IdentifiableResourceReloader {
    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return BovinesAndButtercups.asResource("cow_textures");
    }
}
