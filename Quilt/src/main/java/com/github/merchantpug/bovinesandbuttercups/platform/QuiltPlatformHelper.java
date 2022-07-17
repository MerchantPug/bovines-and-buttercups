package com.github.merchantpug.bovinesandbuttercups.platform;

import com.github.merchantpug.bovinesandbuttercups.mixin.CriteriaTriggersAccessor;
import com.github.merchantpug.bovinesandbuttercups.network.IPacket;
import com.github.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.block.extensions.api.client.BlockRenderLayerMap;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.util.function.Supplier;

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
    public <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        BlockRenderLayerMap.put(renderType, block);
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer player, IPacket packet) {
        ServerPlayNetworking.send(player, packet.getFabricId(), packet.buf());
    }

    @Override
    public void sendPacketToAllPlayers(ServerLevel serverLevel, IPacket packet) {
        for (ServerPlayer player : serverLevel.players()) {
            ServerPlayNetworking.send(player, packet.getFabricId(), packet.buf());
        }
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaTriggersAccessor.callRegister(criterionTrigger);
    }
}
