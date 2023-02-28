package net.merchantpug.bovinesandbuttercups.platform;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistriesQuilt;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
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
    public void registerDefaultConfiguredCowTypes() {
        BovineRegistriesQuilt.COW_TYPE.forEach(cowType -> {
            ConfiguredCowTypeRegistry.register(cowType.getDefaultCowType().getFirst(), cowType.getDefaultCowType().getSecond());
        });
    }

    @Override
    public Codec<CowType<?>> getCowTypeCodec() {
        return BovineRegistriesQuilt.COW_TYPE.byNameCodec();
    }

    @Override
    public Codec<EntityConditionType<?>> getEntityConditionTypeCodec() {
        return BovineRegistriesQuilt.ENTITY_CONDITION_TYPE.byNameCodec();
    }

    @Override
    public Codec<BlockConditionType<?>> getBlockConditionTypeCodec() {
        return BovineRegistriesQuilt.BLOCK_CONDITION_TYPE.byNameCodec();
    }

    @Override
    public Codec<BiomeConditionType<?>> getBiomeConditionTypeCodec() {
        return BovineRegistriesQuilt.BIOME_CONDITION_TYPE.byNameCodec();
    }

    @Override
    public ResourceLocation getCowTypeKey(CowType<?> cowType) {
        return BovineRegistriesQuilt.COW_TYPE.getKey(cowType);
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