package net.merchantpug.bovinesandbuttercups.platform.services;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    void setRenderLayer(Block block, RenderType renderType);

    Map<Block, Block> getPottedBlockMap();
}