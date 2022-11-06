package net.merchantpug.bovinesandbuttercups.platform;

import net.merchantpug.bovinesandbuttercups.content.block.entity.*;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomHugeMushroomItem;
import net.merchantpug.bovinesandbuttercups.content.item.CustomMushroomItem;
import net.merchantpug.bovinesandbuttercups.platform.services.IPlatformHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypesFabriclike;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriclike;
import net.merchantpug.bovinesandbuttercups.registry.BovineItemsFabriclike;
import com.google.auto.service.AutoService;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Map;
import java.util.function.Supplier;

@AutoService(IPlatformHelper.class)
public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
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
    public Map<Block, Block> getPottedBlockMap() {
        return FlowerPotBlock.POTTED_BY_CONTENT;
    }

    @Override
    public CriterionTrigger<?> registerCriteria(CriterionTrigger<?> criterionTrigger) {
        return CriteriaAccessor.callRegister(criterionTrigger);
    }

    @Override
    public BlockEntityType<CustomFlowerBlockEntity> getCustomFlowerBlockEntity() {
        return BovineBlockEntityTypesFabriclike.CUSTOM_FLOWER;
    }

    @Override
    public BlockEntityType<CustomMushroomBlockEntity> getCustomMushroomBlockEntity() {
        return BovineBlockEntityTypesFabriclike.CUSTOM_MUSHROOM;
    }

    @Override
    public BlockEntityType<CustomHugeMushroomBlockEntity> getCustomHugeMushroomBlockEntity() {
        return BovineBlockEntityTypesFabriclike.CUSTOM_MUSHROOM_BLOCK;
    }

    @Override
    public BlockEntityType<CustomFlowerPotBlockEntity> getCustomFlowerPotBlockEntity() {
        return BovineBlockEntityTypesFabriclike.POTTED_CUSTOM_FLOWER;
    }

    @Override
    public BlockEntityType<CustomMushroomPotBlockEntity> getCustomMushroomPotBlockEntity() {
        return BovineBlockEntityTypesFabriclike.POTTED_CUSTOM_MUSHROOM;
    }

    @Override
    public EntityType<FlowerCow> getMoobloomEntity() {
        return BovineEntityTypesFabriclike.MOOBLOOM;
    }

    @Override
    public CustomFlowerItem getCustomFlowerItem() {
        return BovineItemsFabriclike.CUSTOM_FLOWER;
    }

    @Override
    public CustomMushroomItem getCustomMushroomItem() {
        return BovineItemsFabriclike.CUSTOM_MUSHROOM;
    }

    @Override
    public CustomHugeMushroomItem getCustomHugeMushroomItem() {
        return BovineItemsFabriclike.CUSTOM_MUSHROOM_BLOCK;
    }

    @Override
    public Supplier<MobEffect> getLockdownEffectSupplier() {
        return LockdownEffect::new;
    }
}