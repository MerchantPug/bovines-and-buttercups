package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.resources.BovineBlockstateTypes;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.stream.Stream;

public class BovinesAndButtercupsClient {
    public static final HashSet<ResourceLocation> LOADED_COW_TEXTURES = new HashSet<>();
    private static final HashSet<String> COW_TEXTURE_PATHS = new HashSet<>();

    public static void init() {
        BovineBlockstateTypes.init();
        registerCowTexturePath("textures/entity/cow");
        registerCowTexturePath("textures/entity/mooshroom");
        registerCowTexturePath("textures/entity/moobloom");

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
        Services.PLATFORM.setRenderLayer(BovineBlocks.HYACINTH.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_HYACINTH.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.SNOWDROP.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_SNOWDROP.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.TROPICAL_BLUE.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_TROPICAL_BLUE.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.CUSTOM_FLOWER.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_CUSTOM_FLOWER.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.CUSTOM_MUSHROOM.get(), RenderType.cutout());
        Services.PLATFORM.setRenderLayer(BovineBlocks.POTTED_CUSTOM_MUSHROOM.get(), RenderType.cutout());
    }

    public static boolean registerCowTexturePath(String path) {
        if (!COW_TEXTURE_PATHS.contains(path)) {
            return COW_TEXTURE_PATHS.add(path);
        }
        BovinesAndButtercups.LOG.warn("Tried registering cow texture path '{}' more than once. (Skipping).", path);
        return false;
    }

    public static Stream<String> cowTexturePathsAsStream() {
        return COW_TEXTURE_PATHS.stream();
    }
}
