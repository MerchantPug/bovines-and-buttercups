package net.merchantpug.bovinesandbuttercups.platform;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.loader.api.FabricLoader;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesFabriclike;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;

import java.util.Map;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void registerDefaultConfiguredCowTypes() {
        BovineRegistriesFabriclike.COW_TYPE.forEach(cowType -> {
            ConfiguredCowTypeRegistry.register(cowType.getDefaultCowType().getFirst(), cowType.getDefaultCowType().getSecond());
        });
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return BovineRegistriesFabriclike.COW_TYPE.byNameCodec();
    }

    @Override
    public ResourceLocation getCowTypeKey(CowType<?> cowType) {
        return BovineRegistriesFabriclike.COW_TYPE.getKey(cowType);
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
    }

    @Override
    public Map<Block, Block> getPottedBlockMap() {
        return FlowerPotBlock.POTTED_BY_CONTENT;
    }
}