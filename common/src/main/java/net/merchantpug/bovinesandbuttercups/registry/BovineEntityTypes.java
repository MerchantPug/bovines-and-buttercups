package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class BovineEntityTypes {
    private static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(Registries.ENTITY_TYPE, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<EntityType<FlowerCow>> MOOBLOOM = register("moobloom", Services.REGISTRY.createMoobloomEntity());

    public static void register() {

    }

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, Supplier<EntityType<T>> entityType) {
        return ENTITY_TYPES.register(name, entityType);
    }
}
