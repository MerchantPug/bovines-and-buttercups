package net.merchantpug.bovinesandbuttercups.platform.services;

import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;
import java.util.function.Supplier;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);

    void setRenderLayer(Block block, RenderType renderType);

    Map<Block, Block> getPottedBlockMap();

    CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger);

    BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity();

    BlockEntityType<CustomMushroomBlockEntity> getCustomMushroomBlockEntity();

    BlockEntityType<CustomHugeMushroomBlockEntity> getCustomHugeMushroomBlockEntity();

    BlockEntityType<CustomFlowerPotBlockEntity> getCustomFlowerPotBlockEntity();

    BlockEntityType<CustomMushroomPotBlockEntity> getCustomMushroomPotBlockEntity();

    EntityType<? extends FlowerCow> getMoobloomEntity();

    CustomFlowerItem getCustomFlowerItem();

    CustomMushroomItem getCustomMushroomItem();

    CustomHugeMushroomItem getCustomHugeMushroomItem();

    Supplier<MobEffect> getLockdownEffectSupplier();
}