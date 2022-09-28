package net.merchantpug.bovinesandbuttercups.api;

import com.mojang.serialization.Codec;

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
}
