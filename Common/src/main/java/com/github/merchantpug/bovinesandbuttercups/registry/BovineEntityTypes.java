package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class BovineEntityTypes {
    public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(Registry.ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<EntityType<FlowerCow>> MOOBLOOM = register("moobloom", () -> EntityType.Builder.of(FlowerCow::new, MobCategory.CREATURE).sized(0.9f, 1.4f).clientTrackingRange(10).build(Constants.resourceLocation("moobloom").toString()));

    public static void init() {

    }

    public static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType<T>> entityType) {
        return ENTITY_TYPES.register(name, entityType);
    }
}
