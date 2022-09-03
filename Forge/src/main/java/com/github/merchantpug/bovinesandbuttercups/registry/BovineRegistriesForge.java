package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.api.CowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BovineRegistriesForge {
    public static final DeferredRegister<CowType<?>> COW_TYPE = DeferredRegister.create(BovineRegistryKeys.COW_TYPE_KEY.location(), Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<CowType<?>>> COW_TYPE_REGISTRY = COW_TYPE.makeRegistry(() -> new RegistryBuilder<CowType<?>>().setDefaultKey(Constants.resourceLocation("moobloom")).disableSaving().hasTags());

    public static final DeferredRegister<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = DeferredRegister.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY.location(), Constants.MOD_ID);
    public static final Supplier<IForgeRegistry<ConfiguredCowType<?, ?>>> CONFIGURED_COW_TYPE_REGISTRY = CONFIGURED_COW_TYPE.makeRegistry(() -> new RegistryBuilder<ConfiguredCowType<?, ?>>().disableSaving().dataPackRegistry(ConfiguredCowType.CODEC, ConfiguredCowType.CODEC).hasTags());

    public static void init(IEventBus eventBus) {
        COW_TYPE.register(eventBus);
        COW_TYPE.register("moobloom", () -> BovineCowTypes.FLOWER_COW_TYPE);
        COW_TYPE.register("mooshroom", () -> BovineCowTypes.MUSHROOM_COW_TYPE);

        CONFIGURED_COW_TYPE.register(eventBus);
        CONFIGURED_COW_TYPE.register("missing_moobloom", () -> new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
        CONFIGURED_COW_TYPE.register("missing_mooshroom", () -> new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));
    }
}