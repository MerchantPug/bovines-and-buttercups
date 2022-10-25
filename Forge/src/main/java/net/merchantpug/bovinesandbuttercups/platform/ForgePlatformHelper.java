package net.merchantpug.bovinesandbuttercups.platform;

import net.merchantpug.bovinesandbuttercups.block.entity.*;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffectForge;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import net.merchantpug.bovinesandbuttercups.registry.*;
import com.google.auto.service.AutoService;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Supplier;

@AutoService(IPlatformHelper.class)
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
        // Handled through block model JSON
        // ItemBlockRenderTypes.setRenderLayer(block, renderType);
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
    public BlockEntityType<CustomMushroomBlockEntity> getCustomMushroomBlockEntity() {
        return BovineBlockEntityTypesForge.CUSTOM_MUSHROOM.get();
    }

    @Override
    public BlockEntityType<CustomHugeMushroomBlockEntity> getCustomHugeMushroomBlockEntity() {
        return BovineBlockEntityTypesForge.CUSTOM_MUSHROOM_BLOCK.get();
    }

    @Override
    public BlockEntityType<CustomFlowerPotBlockEntity> getCustomFlowerPotBlockEntity() {
        return BovineBlockEntityTypesForge.POTTED_CUSTOM_FLOWER.get();
    }

    @Override
    public BlockEntityType<CustomMushroomPotBlockEntity> getCustomMushroomPotBlockEntity() {
        return BovineBlockEntityTypesForge.POTTED_CUSTOM_MUSHROOM.get();
    }

    @Override
    public EntityType<FlowerCow> getMoobloomEntity() {
        return BovineEntityTypesForge.MOOBLOOM.get();
    }

    @Override
    public CustomFlowerItem getCustomFlowerItem() {
        return BovineItemsForge.CUSTOM_FLOWER.get();
    }

    @Override
    public CustomMushroomItem getCustomMushroomItem() {
        return BovineItemsForge.CUSTOM_MUSHROOM.get();
    }

    @Override
    public CustomHugeMushroomItem getCustomHugeMushroomItem() {
        return BovineItemsForge.CUSTOM_MUSHROOM_BLOCK.get();
    }

    @Override
    public Supplier<MobEffect> getLockdownEffectSupplier() {
        return LockdownEffectForge::new;
    }
}
