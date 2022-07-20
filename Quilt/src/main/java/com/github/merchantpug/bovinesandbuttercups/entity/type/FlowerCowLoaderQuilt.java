package com.github.merchantpug.bovinesandbuttercups.entity.type;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.entity.type.flower.FlowerCowLoader;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class FlowerCowLoaderQuilt extends FlowerCowLoader implements IdentifiableResourceReloader {

    @Override
    public ResourceLocation getQuiltId() {
        return BovinesAndButtercupsCommon.resourceLocation("moobloom_types");
    }
}
