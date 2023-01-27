package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;

import java.util.function.Supplier;

public class BovineCowTypes {
    public static final RegistrationProvider<CowType<?>> COW_TYPE = RegistrationProvider.get(BovineRegistryKeys.COW_TYPE_KEY, BovinesAndButtercups.MOD_ID);

    public static final Supplier<CowType<FlowerCowConfiguration>> FLOWER_COW_TYPE = register("moobloom", () -> new CowType<>(FlowerCowConfiguration::getCodec));
    public static final Supplier<CowType<MushroomCowConfiguration>> MUSHROOM_COW_TYPE = register("mooshroom", () -> new CowType<>(MushroomCowConfiguration::getCodec));

    public static void register() {
    }

    public static void registerDefaultConfigureds() {
        FLOWER_COW_TYPE.get().setDefaultConfiguredCowType(BovinesAndButtercups.asResource("missing_moobloom"), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE.get(), FlowerCowConfiguration.MISSING, Integer.MAX_VALUE));
        MUSHROOM_COW_TYPE.get().setDefaultConfiguredCowType(BovinesAndButtercups.asResource("missing_mooshroom"), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE.get(), MushroomCowConfiguration.MISSING, Integer.MAX_VALUE));
    }

    private static <CTC extends CowTypeConfiguration> RegistryObject<CowType<CTC>> register(String name, Supplier<CowType<CTC>> cowType) {
        return COW_TYPE.register(name, cowType);
    }
}
