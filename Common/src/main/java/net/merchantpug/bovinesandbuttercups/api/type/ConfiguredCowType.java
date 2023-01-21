package net.merchantpug.bovinesandbuttercups.api.type;

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
    private final int loadingPriority;

    public ConfiguredCowType(CT cowType, CTC configuration, int loadingPriority) {
        this.cowType = cowType;
        this.configuration = configuration;
        this.loadingPriority = loadingPriority;
    }

    public CT getCowType() {
        return cowType;
    }

    public CTC getConfiguration() {
        return configuration;
    }

    public int getLoadingPriority() {
        return loadingPriority;
    }
}
