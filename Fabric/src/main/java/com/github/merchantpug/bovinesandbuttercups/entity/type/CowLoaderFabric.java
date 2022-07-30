package com.github.merchantpug.bovinesandbuttercups.entity.type;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.CowLoader;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class CowLoaderFabric extends CowLoader implements IdentifiableResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {
        return Constants.resourceLocation("cow_types");
    }
}
