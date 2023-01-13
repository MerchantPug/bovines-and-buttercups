package net.merchantpug.bovinesandbuttercups.platform;

import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;

import java.util.Map;

@AutoService(IPlatformHelper.class)
public class QuiltPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Quilt";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return QuiltLoader.isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return QuiltLoader.isDevelopmentEnvironment();
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        BlockRenderLayerMap.put(renderType, block);
    }

    @Override
    public Map<Block, Block> getPottedBlockMap() {
        return FlowerPotBlock.POTTED_BY_CONTENT;
    }
}