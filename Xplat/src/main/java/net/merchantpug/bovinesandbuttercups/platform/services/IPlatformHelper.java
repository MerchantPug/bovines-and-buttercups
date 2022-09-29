package net.merchantpug.bovinesandbuttercups.platform.services;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.block.entity.*;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.LevelAccessor;
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

    ResourceLocation getMushroomCowTypeResourceLocation(MushroomCow cow);

    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowTypeFromCow(MushroomCow cow);

    void setMushroomCowType(MushroomCow cow, ResourceLocation cowTypeKey);

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
