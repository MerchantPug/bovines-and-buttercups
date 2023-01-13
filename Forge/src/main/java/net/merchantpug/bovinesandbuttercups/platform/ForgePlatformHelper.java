package net.merchantpug.bovinesandbuttercups.platform;

import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.util.PottedBlockMapUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.Map;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        // Handled through block model JSON
    }

    @Override
    public Map<Block, Block> getPottedBlockMap() {
        return PottedBlockMapUtil.getPottedContentMap();
    }
}
