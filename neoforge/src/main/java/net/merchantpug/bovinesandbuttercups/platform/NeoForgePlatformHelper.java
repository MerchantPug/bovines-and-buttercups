package net.merchantpug.bovinesandbuttercups.platform;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesNeoForge;
import net.merchantpug.bovinesandbuttercups.util.PottedBlockMapUtil;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.util.thread.EffectiveSide;

import java.util.Map;

@AutoService(IPlatformHelper.class)
public class NeoForgePlatformHelper implements IPlatformHelper {
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
    public void registerDefaultConfiguredCowTypes() {
        BovineRegistriesNeoForge.COW_TYPE.forEach(cowType -> {
            ConfiguredCowTypeRegistry.register(cowType.getDefaultCowType().getFirst(), cowType.getDefaultCowType().getSecond());
        });
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(BovineRegistriesNeoForge.COW_TYPE::byNameCodec);
    }

    @Override
    public Codec<EntityConditionType<?>> getEntityConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(BovineRegistriesNeoForge.ENTITY_CONDITION_TYPE::byNameCodec);
    }

    @Override
    public Codec<BlockConditionType<?>> getBlockConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(BovineRegistriesNeoForge.BLOCK_CONDITION_TYPE::byNameCodec);
    }

    @Override
    public Codec<BiomeConditionType<?>> getBiomeConditionTypeCodec() {
        return ExtraCodecs.lazyInitializedCodec(BovineRegistriesNeoForge.BIOME_CONDITION_TYPE::byNameCodec);
    }

    @Override
    public ResourceLocation getCowTypeKey(CowType<?> cowType) {
        return BovineRegistriesNeoForge.COW_TYPE.getKey(cowType);
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
