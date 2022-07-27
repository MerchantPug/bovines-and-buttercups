package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static com.github.merchantpug.bovinesandbuttercups.Constants.MOD_ID;

public class BovinesAndButtercupsCommonClient {
    public static void init() {
        Services.PLATFORM.setRenderLayer(BovineBlocks.BUTTERCUP.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_BUTTERCUP.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.PINK_DAISY.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_PINK_DAISY.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.LIMELIGHT.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_LIMELIGHT.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.BIRD_OF_PARADISE.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_BIRD_OF_PARADISE.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.CHARGELILY.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_CHARGELILY.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.CUSTOM_FLOWER.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_CUSTOM_FLOWER.get(), RenderType.cutout());
    }
}