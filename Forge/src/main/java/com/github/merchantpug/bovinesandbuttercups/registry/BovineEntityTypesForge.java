package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.block.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BovineEntityTypesForge {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);

    public static final RegistryObject<EntityType<FlowerCow>> MOOBLOOM = ENTITY_TYPES.register("moobloom", () -> EntityType.Builder.of(FlowerCow::new, MobCategory.CREATURE).build(Constants.resourceLocation("moobloom").toString()));

    public static void init() {
        ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
