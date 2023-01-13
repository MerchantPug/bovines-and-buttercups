package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCowFabriclike;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.platform.services.IRegistryHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import org.quiltmc.qsl.block.entity.api.QuiltBlockEntityTypeBuilder;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

import java.util.function.Supplier;

@AutoService(IRegistryHelper.class)
public class QuiltRegistryHelper implements IRegistryHelper {
    @Override
    public <T extends Mob> SpawnEggItem createSpawnEggItem(Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
    }

    @Override
    public NectarBowlItem createNectarBowlItem(Item.Properties properties) {
        return new NectarBowlItem(new Item.Properties().stacksTo(1));
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaAccessor.callRegister(criterionTrigger);
    }

    @Override
    public Supplier<BlockEntityType<CustomFlowerBlockEntity>> createCustomFlowerBlockEntity() {
        return () -> QuiltBlockEntityTypeBuilder.create(CustomFlowerBlockEntity::new, BovineBlocks.CUSTOM_FLOWER.get()).build(null);
    }

    @Override
    public Supplier<BlockEntityType<CustomMushroomBlockEntity>> createCustomMushroomBlockEntity() {
        return () -> QuiltBlockEntityTypeBuilder.create(CustomMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM.get()).build(null);
    }

    @Override
    public Supplier<BlockEntityType<CustomHugeMushroomBlockEntity>> createCustomHugeMushroomBlockEntity() {
        return () -> QuiltBlockEntityTypeBuilder.create(CustomHugeMushroomBlockEntity::new, BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()).build(null);
    }

    @Override
    public Supplier<BlockEntityType<CustomFlowerPotBlockEntity>> createCustomFlowerPotBlockEntity() {
        return () -> QuiltBlockEntityTypeBuilder.create(CustomFlowerPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_FLOWER.get()).build(null);
    }

    @Override
    public Supplier<BlockEntityType<CustomMushroomPotBlockEntity>> createCustomMushroomPotBlockEntity() {
        return () -> QuiltBlockEntityTypeBuilder.create(CustomMushroomPotBlockEntity::new, BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()).build(null);
    }

    @Override
    public <T extends FlowerCow> Supplier<EntityType<T>> createMoobloomEntity() {
        return () -> (EntityType<T>) QuiltEntityTypeBuilder.Mob.createMob().spawnGroup(MobCategory.CREATURE).entityFactory(FlowerCowFabriclike::new).setDimensions(EntityDimensions.scalable(0.9F, 1.4F)).spawnRestriction(SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn).defaultAttributes(Cow.createAttributes()).maxChunkTrackingRange(10).build();
    }

    @Override
    public Supplier<CustomFlowerItem> createCustomFlowerItem() {
        return () -> new CustomFlowerItem(BovineBlocks.CUSTOM_FLOWER.get(), new Item.Properties());
    }

    @Override
    public Supplier<CustomMushroomItem> createCustomMushroomItem() {
        return () -> new CustomMushroomItem(BovineBlocks.CUSTOM_MUSHROOM.get(), new Item.Properties());
    }

    @Override
    public Supplier<CustomHugeMushroomItem> createCustomHugeMushroomItem() {
        return () -> new CustomHugeMushroomItem(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get(), new Item.Properties());
    }

    @Override
    public Supplier<MobEffect> createLockdownEffectSupplier() {
        return LockdownEffect::new;
    }
}
