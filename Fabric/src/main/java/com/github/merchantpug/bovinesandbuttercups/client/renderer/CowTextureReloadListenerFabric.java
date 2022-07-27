package com.github.merchantpug.bovinesandbuttercups.client.renderer;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommonClient;
import com.github.merchantpug.bovinesandbuttercups.client.resources.CowTextureReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Set;

public class CowTextureReloadListenerFabric extends CowTextureReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return BovinesAndButtercupsCommon.resourceLocation("cow_textures");
    }
}
