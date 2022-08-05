package com.github.merchantpug.bovinesandbuttercups.platform;

import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import com.github.merchantpug.bovinesandbuttercups.network.BovineForgePacketHandler;
import com.github.merchantpug.bovinesandbuttercups.network.IPacket;
import com.github.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypesForge;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesForge;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineItemsForge;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

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
    public <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
    }

    @Override
    public void setRenderLayer(Block block, RenderType renderType) {
        ItemBlockRenderTypes.setRenderLayer(block, renderType);
    }

    @Override
    public void sendPacketToPlayer(ServerPlayer player, IPacket packet) {
        BovineForgePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    @Override
    public void sendPacketToAllPlayers(ServerLevel serverLevel, IPacket packet) {
        BovineForgePacketHandler.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaTriggers.register(criterionTrigger);
    }

    @Override
    public BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity() {
        return BovineBlockEntityTypesForge.CUSTOM_FLOWER.get();
    }

    @Override
    public EntityType<FlowerCow> getMoobloomEntity() {
        return BovineEntityTypesForge.MOOBLOOM.get();
    }

    @Override
    public CustomFlowerItem getCustomFlowerItem() {
        return BovineItemsForge.CUSTOM_FLOWER.get();
    }
}
