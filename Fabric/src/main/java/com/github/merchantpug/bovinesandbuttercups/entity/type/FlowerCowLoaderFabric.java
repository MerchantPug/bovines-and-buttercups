package com.github.merchantpug.bovinesandbuttercups.entity.type;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.entity.type.flower.FlowerCowLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class FlowerCowLoaderFabric extends FlowerCowLoader implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercupsCommon.resourceLocation("moobloom_types");
    }
}
