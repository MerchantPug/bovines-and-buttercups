package net.merchantpug.bovinesandbuttercups.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
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

    public static void init(IEventBus eventBus) {
        COW_TYPE.register(eventBus);
        COW_TYPE.register("moobloom", () -> BovineCowTypes.FLOWER_COW_TYPE);
        COW_TYPE.register("mooshroom", () -> BovineCowTypes.MUSHROOM_COW_TYPE);

        CONFIGURED_COW_TYPE.register(eventBus);
        CONFIGURED_COW_TYPE.register("missing_moobloom", () -> new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE, FlowerCowConfiguration.MISSING));
        CONFIGURED_COW_TYPE.register("missing_mooshroom", () -> new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE, MushroomCowConfiguration.MISSING));

        FLOWER_TYPE.register(eventBus);
        FLOWER_TYPE.register("missing_flower", () -> FlowerType.MISSING);

        MUSHROOM_TYPE.register(eventBus);
        MUSHROOM_TYPE.register("missing_mushroom", () -> MushroomType.MISSING);
    }

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}