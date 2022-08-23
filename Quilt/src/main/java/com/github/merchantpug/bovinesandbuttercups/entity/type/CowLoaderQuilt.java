package com.github.merchantpug.bovinesandbuttercups.entity.type;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.CowLoader;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class CowLoaderQuilt extends CowLoader implements IdentifiableResourceReloader {

    @Override
    public @NotNull ResourceLocation getQuiltId() {
        return Constants.resourceLocation("cow_types");
    }
}
