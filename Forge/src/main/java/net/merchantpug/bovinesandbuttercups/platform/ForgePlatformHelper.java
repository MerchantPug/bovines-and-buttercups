package net.merchantpug.bovinesandbuttercups.platform;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesForge;
import net.merchantpug.bovinesandbuttercups.util.PottedBlockMapUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;

@AutoService(IPlatformHelper.class)
public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isClientSide() {
        return EffectiveSide.get().isClient();
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
    public MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    @Override
    public void registerDefaultConfiguredCowTypes() {
        BovineRegistriesForge.COW_TYPE_REGISTRY.get().forEach(cowType -> {
            ConfiguredCowTypeRegistry.register(cowType.getDefaultCowType().getFirst(), cowType.getDefaultCowType().getSecond());
        });
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(() -> BovineRegistriesForge.COW_TYPE_REGISTRY.get().getCodec());
    }

    @Override
    public Codec<EntityConditionType<?>> getEntityConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(() -> BovineRegistriesForge.ENTITY_CONDITION_TYPE_REGISTRY.get().getCodec());
    }

    @Override
    public Codec<BlockConditionType<?>> getBlockConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(() -> BovineRegistriesForge.BLOCK_CONDITION_TYPE_REGISTRY.get().getCodec());
    }

    @Override
    public Codec<BiomeConditionType<?>> getBiomeConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(() -> BovineRegistriesForge.BIOME_CONDITION_TYPE_REGISTRY.get().getCodec());
    }

    @Override
    public ResourceLocation getCowTypeKey(CowType<?> cowType) {
        return BovineRegistriesForge.COW_TYPE_REGISTRY.get().getKey(cowType);
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
