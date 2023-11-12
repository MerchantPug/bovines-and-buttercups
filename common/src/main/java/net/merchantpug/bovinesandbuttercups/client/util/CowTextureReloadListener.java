package net.merchantpug.bovinesandbuttercups.client.util;

import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashSet;
import java.util.Set;

public class CowTextureReloadListener extends SimplePreparableReloadListener<Set<ResourceLocation>> {
    @Override
    protected Set<ResourceLocation> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsClient.LOADED_COW_TEXTURES.clear();
        Set<ResourceLocation> cowTextures = new HashSet<>();
        BovinesAndButtercupsClient.cowTexturePathsAsStream().forEach(path ->
                cowTextures.addAll(resourceManager.listResources(path, resourceLocation -> resourceLocation.getPath().endsWith(".png")).keySet()));
        return cowTextures;
    }

    @Override
    protected void apply(Set<ResourceLocation> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        BovinesAndButtercupsClient.LOADED_COW_TEXTURES.addAll(object);
    }
}
