package net.merchantpug.bovinesandbuttercups.api.type;

import com.mojang.serialization.Codec;

public class ConfiguredCowType<CTC extends CowTypeConfiguration, CT extends CowType<CTC>> {

    private final CT cowType;
    private final CTC configuration;
    private final int loadingPriority;

    public ConfiguredCowType(CT cowType, CTC configuration, int loadingPriority) {
        this.cowType = cowType;
        this.configuration = configuration;
        this.loadingPriority = loadingPriority;
    }

    public static Codec<ConfiguredCowType<?, ?>> getServerCodec() {
        return CowType.CODEC.dispatch(ConfiguredCowType::getCowType, CowType::getServerCodec);
    }

    public static Codec<ConfiguredCowType<?, ?>> getClientCodec() {
        return CowType.CODEC.dispatch(ConfiguredCowType::getCowType, CowType::getClientCodec);
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
