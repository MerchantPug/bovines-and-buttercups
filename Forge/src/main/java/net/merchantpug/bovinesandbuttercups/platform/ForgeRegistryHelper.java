package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffectForge;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCowForge;
import net.merchantpug.bovinesandbuttercups.content.item.*;
import net.merchantpug.bovinesandbuttercups.platform.services.IRegistryHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;

import java.util.function.Supplier;

@AutoService(IRegistryHelper.class)
public class ForgeRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
    }

    @Override
    public NectarBowlItem createNectarBowlItem(Item.Properties properties) {
        return new NectarBowlItem(properties);
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaTriggers.register(criterionTrigger);
    }

    @Override
    public Supplier<BlockEntityType<CustomFlowerBlockEntity>> createCustomFlowerBlockEntity() {
        return () -> BlockEntityType.Builder.of(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_flower").toString()));
    }

    @Override
    public Supplier<BlockEntityType<CustomMushroomBlockEntity>> createCustomMushroomBlockEntity() {
        return () -> BlockEntityType.Builder.of(CustomMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom").toString()));
    }

    @Override
    public Supplier<BlockEntityType<CustomHugeMushroomBlockEntity>> createCustomHugeMushroomBlockEntity() {
        return () -> BlockEntityType.Builder.of(CustomHugeMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("custom_mushroom_block").toString()));
    }

    @Override
    public Supplier<BlockEntityType<CustomFlowerPotBlockEntity>> createCustomFlowerPotBlockEntity() {
        return () -> BlockEntityType.Builder.of(CustomFlowerPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_FLOWER.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_flower").toString()));
    }

    @Override
    public Supplier<BlockEntityType<CustomMushroomPotBlockEntity>> createCustomMushroomPotBlockEntity() {
        return () -> BlockEntityType.Builder.of(CustomMushroomPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, BovinesAndButtercups.asResource("potted_custom_mushroom").toString()));
    }

    @Override
    public <T extends FlowerCow> Supplier<EntityType<T>> createMoobloomEntity() {
        return () -> (EntityType<T>) EntityType.Builder.of(FlowerCowForge::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(BovinesAndButtercups.asResource("moobloom").toString());
    }

    @Override
    public Supplier<CustomFlowerItem> createCustomFlowerItem() {
        return () -> new CustomFlowerItemForge(BovineBlocks.CUSTOM_FLOWER.get(), new Item.Properties());
    }

    @Override
    public Supplier<CustomMushroomItem> createCustomMushroomItem() {
        return () -> new CustomMushroomItemForge(BovineBlocks.CUSTOM_MUSHROOM.get(), new Item.Properties());
    }

    @Override
    public Supplier<CustomHugeMushroomItem> createCustomHugeMushroomItem() {
        return () -> new CustomHugeMushroomItemForge(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get(), new Item.Properties());
    }

    @Override
    public Supplier<MobEffect> createLockdownEffectSupplier() {
        return LockdownEffectForge::new;
    }
}
