package com.github.merchantpug.bovinesandbuttercups.platform.services;

import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import com.github.merchantpug.bovinesandbuttercups.network.IPacket;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);

    void setRenderLayer(Block block, RenderType renderType);

    void sendPacketToPlayer(ServerPlayer player, IPacket packet);

    void sendPacketToAllPlayers(ServerLevel serverLevel, IPacket packet);

    CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger);

    BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity();

    EntityType<FlowerCow> getMoobloomEntity();

    CustomFlowerItem getCustomFlowerItem();
}
