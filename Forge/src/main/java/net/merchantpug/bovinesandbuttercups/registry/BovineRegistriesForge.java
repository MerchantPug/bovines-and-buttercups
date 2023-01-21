package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BovineRegistriesForge {
    public static final DeferredRegister<CowType<?>> COW_TYPE = DeferredRegister.create(BovineRegistryKeys.COW_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<CowType<?>>> COW_TYPE_REGISTRY = COW_TYPE.makeRegistry(() -> new RegistryBuilder<CowType<?>>().disableSaving().hasTags());

    public static void init(IEventBus eventBus) {
        COW_TYPE.register(eventBus);
    }
}