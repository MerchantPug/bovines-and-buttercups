package com.github.merchantpug.bovinesandbuttercups;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashSet;
import java.util.Set;

public class CowTextureReloadListener extends SimplePreparableReloadListener<Set<ResourceLocation>> {
    @Override
    protected Set<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsXplatClient.LOADED_COW_TEXTURES.clear();
        Set<ResourceLocation> cowTextures = new HashSet<>();
        cowTextures.addAll(resourceManager.listResources("textures/entity/cow", resourceLocation -> resourceLocation.getPath().endsWith(".png")).keySet());
        cowTextures.addAll(resourceManager.listResources("textures/entity/moobloom", resourceLocation -> resourceLocation.getPath().endsWith(".png")).keySet());
        cowTextures.addAll(resourceManager.listResources("textures/entity/mooshroom", resourceLocation -> resourceLocation.getPath().endsWith(".png")).keySet());
        return cowTextures;
    }

    @Override
    protected void apply(Set<ResourceLocation> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsXplatClient.LOADED_COW_TEXTURES.addAll(object);
    }
}
