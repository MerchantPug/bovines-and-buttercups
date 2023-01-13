package net.merchantpug.bovinesandbuttercups.platform.services;

import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface IRegistryHelper {
    <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);

    NectarBowlItem createNectarBowlItem(Item.Properties properties);

    CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger);

    Supplier<BlockEntityType<CustomFlowerBlockEntity>> createCustomFlowerBlockEntity();

    Supplier<BlockEntityType<CustomMushroomBlockEntity>> createCustomMushroomBlockEntity();

    Supplier<BlockEntityType<CustomHugeMushroomBlockEntity>> createCustomHugeMushroomBlockEntity();

    Supplier<BlockEntityType<CustomFlowerPotBlockEntity>> createCustomFlowerPotBlockEntity();

    Supplier<BlockEntityType<CustomMushroomPotBlockEntity>> createCustomMushroomPotBlockEntity();

    <T extends FlowerCow> Supplier<EntityType<T>> createMoobloomEntity();

    Supplier<CustomFlowerItem> createCustomFlowerItem();

    Supplier<CustomMushroomItem> createCustomMushroomItem();

    Supplier<CustomHugeMushroomItem> createCustomHugeMushroomItem();

    Supplier<MobEffect> createLockdownEffectSupplier();
}
