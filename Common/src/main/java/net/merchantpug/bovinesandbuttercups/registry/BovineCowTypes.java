package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import java.util.function.Supplier;

public class BovineCowTypes {
    private static final RegistrationProvider<CowType<?>> COW_TYPE = RegistrationProvider.get(BovineRegistryKeys.COW_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final Supplier<CowType<FlowerCowConfiguration>> FLOWER_COW_TYPE = register("moobloom", () -> new CowType<>(asMapCodec(FlowerCowConfiguration.CODEC)));
    public static final Supplier<CowType<MushroomCowConfiguration>> MUSHROOM_COW_TYPE = register("mooshroom", () -> new CowType<>(asMapCodec(MushroomCowConfiguration.CODEC)));

    public static void register() {
    }

    public static void registerDefaultConfigureds() {
        FLOWER_COW_TYPE.get().setDefaultConfiguredCowType(BovinesAndButtercups.asResource("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE.get(), FlowerCowConfiguration.MISSING, Integer.MAX_VALUE));
        MUSHROOM_COW_TYPE.get().setDefaultConfiguredCowType(BovinesAndButtercups.asResource("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE.get(), MushroomCowConfiguration.MISSING, Integer.MAX_VALUE));
    }

    private static <CTC extends CowTypeConfiguration> RegistryObject<CowType<CTC>> register(String name, Supplier<CowType<CTC>> cowType) {
        return COW_TYPE.register(name, cowType);
    }

    public static <C> MapCodec<C> asMapCodec(Codec<C> codec) {
        if (codec instanceof MapCodec.MapCodecCodec) {
            return ((MapCodec.MapCodecCodec<C>) codec).codec();
        }
        return codec.fieldOf("value");
    }
}
