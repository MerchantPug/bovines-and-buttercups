package net.merchantpug.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;

public class ConfiguredCowType<CTC extends CowTypeConfiguration, CT extends CowType<CTC>> {
    public static final Codec<ConfiguredCowType<?, ?>> CODEC = CowType.CODEC.dispatch(ConfiguredCowType::getCowType, CowType::getCodec);

    private final CT cowType;
    private final CTC configuration;

    public ConfiguredCowType(CT cowType, CTC configuration) {
        this.cowType = cowType;
        this.configuration = configuration;
    }

    public CT getCowType() {
        return cowType;
    }

    public CTC getConfiguration() {
        return configuration;
    }

    public static Holder<ConfiguredCowType<?, ?>> bootstrap(Registry<ConfiguredCowType<?, ?>> registry) {
        BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovinesAndButtercups.asResource("missing_mooshroom")), new ConfiguredCowType<>(BovineCowTypes.MUSHROOM_COW_TYPE.get(), MushroomCowConfiguration.MISSING));
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.CONFIGURED_COW_TYPE_KEY, BovinesAndButtercups.asResource("missing_moobloom")), new ConfiguredCowType<>(BovineCowTypes.FLOWER_COW_TYPE.get(), FlowerCowConfiguration.MISSING));
    }
}
