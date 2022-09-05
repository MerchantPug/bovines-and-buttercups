package com.github.merchantpug.bovinesandbuttercups.platform.services;

import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.api.CowType;
import com.github.merchantpug.bovinesandbuttercups.block.entity.*;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import com.github.merchantpug.bovinesandbuttercups.item.CustomHugeMushroomItem;
import com.github.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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

    CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger);

    ResourceKey<Registry<ConfiguredCowType<?, ?>>> getConfiguredCowTypeResourceKey();

    Codec<CowType<?>> getCowTypeCodec();

    BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity();

    BlockEntityType<CustomMushroomBlockEntity> getCustomMushroomBlockEntity();

    BlockEntityType<CustomHugeMushroomBlockEntity> getCustomHugeMushroomBlockEntity();

    BlockEntityType<CustomFlowerPotBlockEntity> getCustomFlowerPotBlockEntity();

    BlockEntityType<CustomMushroomPotBlockEntity> getCustomMushroomPotBlockEntity();

    EntityType<FlowerCow> getMoobloomEntity();

    CustomFlowerItem getCustomFlowerItem();

    CustomMushroomItem getCustomMushroomItem();

    CustomHugeMushroomItem getCustomHugeMushroomItem();
}
