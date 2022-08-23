package com.github.merchantpug.bovinesandbuttercups.platform;

import com.github.merchantpug.bovinesandbuttercups.block.entity.*;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import com.github.merchantpug.bovinesandbuttercups.item.CustomHugeMushroomItem;
import com.github.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import com.github.merchantpug.bovinesandbuttercups.network.IPacket;
import com.github.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypesFabriQuilt;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriQuilt;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineItemsFabriQuilt;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
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
import org.quiltmc.loader.api.QuiltLoader;

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
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderType);
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer player, IPacket packet) {
        ServerPlayNetworking.send(player, packet.getFabriQuiltId(), packet.buf());
    }

    @Override
    public void sendPacketToAllPlayers(ServerLevel serverLevel, IPacket packet) {
        for (ServerPlayer player : serverLevel.players()) {
            ServerPlayNetworking.send(player, packet.getFabriQuiltId(), packet.buf());
        }
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaAccessor.callRegister(criterionTrigger);
    }

    @Override
    public BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity() {
        return BovineBlockEntityTypesFabriQuilt.CUSTOM_FLOWER;
    }

    @Override
    public BlockEntityType<CustomMushroomBlockEntity> getCustomMushroomBlockEntity() {
        return BovineBlockEntityTypesFabriQuilt.CUSTOM_MUSHROOM;
    }

    @Override
    public BlockEntityType<CustomHugeMushroomBlockEntity> getCustomHugeMushroomBlockEntity() {
        return BovineBlockEntityTypesFabriQuilt.CUSTOM_MUSHROOM_BLOCK;
    }

    @Override
    public BlockEntityType<CustomFlowerPotBlockEntity> getCustomFlowerPotBlockEntity() {
        return BovineBlockEntityTypesFabriQuilt.POTTED_CUSTOM_FLOWER;
    }

    @Override
    public BlockEntityType<CustomMushroomPotBlockEntity> getCustomMushroomPotBlockEntity() {
        return BovineBlockEntityTypesFabriQuilt.POTTED_CUSTOM_MUSHROOM;
    }

    @Override
    public EntityType<FlowerCow> getMoobloomEntity() {
        return BovineEntityTypesFabriQuilt.MOOBLOOM;
    }

    @Override
    public CustomFlowerItem getCustomFlowerItem() {
        return BovineItemsFabriQuilt.CUSTOM_FLOWER;
    }

    @Override
    public CustomMushroomItem getCustomMushroomItem() {
        return BovineItemsFabriQuilt.CUSTOM_MUSHROOM;
    }

    @Override
    public CustomHugeMushroomItem getCustomHugeMushroomItem() {
        return BovineItemsFabriQuilt.CUSTOM_MUSHROOM_BLOCK;
    }
}