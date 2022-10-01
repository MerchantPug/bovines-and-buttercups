package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class BovineRegistriesForge {
    public static final DeferredRegister<CowType<?>> COW_TYPE = DeferredRegister.create(BovineRegistryKeys.COW_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<CowType<?>>> COW_TYPE_REGISTRY = COW_TYPE.makeRegistry(() -> new RegistryBuilder<CowType<?>>().disableSaving().hasTags());

    public static final DeferredRegister<ConfiguredCowType<?, ?>> CONFIGURED_COW_TYPE = DeferredRegister.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<ConfiguredCowType<?, ?>>> CONFIGURED_COW_TYPE_REGISTRY = CONFIGURED_COW_TYPE.makeRegistry(() -> new RegistryBuilder<ConfiguredCowType<?, ?>>().disableSaving().dataPackRegistry(ConfiguredCowType.CODEC, ConfiguredCowType.CODEC).hasTags());

    public static final DeferredRegister<FlowerType> FLOWER_TYPE = DeferredRegister.create(BovineRegistryKeys.FLOWER_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<FlowerType>> FLOWER_TYPE_REGISTRY = FLOWER_TYPE.makeRegistry(() -> new RegistryBuilder<FlowerType>().disableSaving().dataPackRegistry(FlowerType.CODEC.codec(), FlowerType.CODEC.codec()).hasTags());

    public static final DeferredRegister<MushroomType> MUSHROOM_TYPE = DeferredRegister.create(BovineRegistryKeys.MUSHROOM_TYPE_KEY.location(), BovinesAndButtercups.MOD_ID);
    public static final Supplier<IForgeRegistry<MushroomType>> MUSHROOM_TYPE_REGISTRY = MUSHROOM_TYPE.makeRegistry(() -> new RegistryBuilder<MushroomType>().disableSaving().dataPackRegistry(MushroomType.CODEC.codec(), MushroomType.CODEC.codec()).hasTags());

    public static void initCowTypeRegistry(IEventBus eventBus) {
        COW_TYPE.register(eventBus);
    }

    public static void init(IEventBus eventBus) {
        CONFIGURED_COW_TYPE.register(eventBus);
        CONFIGURED_COW_TYPE.register("missing_moobloom", () -> new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE.get(), FlowerCowConfiguration.MISSING));
        CONFIGURED_COW_TYPE.register("missing_mooshroom", () -> new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE.get(), MushroomCowConfiguration.MISSING));

        FLOWER_TYPE.register(eventBus);
        FLOWER_TYPE.register("missing", () -> FlowerType.MISSING);

        MUSHROOM_TYPE.register(eventBus);
        MUSHROOM_TYPE.register("missing", () -> MushroomType.MISSING);
    }
}