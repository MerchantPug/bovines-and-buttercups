package net.merchantpug.bovinesandbuttercups.platform.services;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.Map;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    void registerDefaultConfiguredCowTypes();

    Codec<CowType<?>> getCowTypeCodec();

    Codec<EntityConditionType<?>> getEntityConditionTypeCodec();

    Codec<BlockConditionType<?>> getBlockConditionTypeCodec();

    Codec<BiomeConditionType<?>> getBiomeConditionTypeCodec();

    ResourceLocation getCowTypeKey(CowType<?> cowType);

    void setRenderLayer(Block block, RenderType renderType);

    Map<Block, Block> getPottedBlockMap();

    void addMoobloomFlowerBlockProperties(StateDefinition.Builder<Block, BlockState> builder);

}