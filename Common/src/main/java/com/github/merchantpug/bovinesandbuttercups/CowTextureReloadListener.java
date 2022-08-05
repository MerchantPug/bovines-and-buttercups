package com.github.merchantpug.bovinesandbuttercups;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Set;

public class CowTextureReloadListener extends SimplePreparableReloadListener<Set<ResourceLocation>> {
    @Override
    protected Set<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsCommonClient.LOADED_COW_TEXTURES.clear();
        return resourceManager.listResources("textures/entity/moobloom", resourceLocation -> resourceLocation.getPath().endsWith(".png")).keySet();
    }

    @Override
    protected void apply(Set<ResourceLocation> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsCommonClient.LOADED_COW_TEXTURES.addAll(object);
    }
}
