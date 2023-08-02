package net.merchantpug.bovinesandbuttercups.api.type;

import com.mojang.serialization.Codec;

public record ConfiguredCowType<CTC extends CowTypeConfiguration, CT extends CowType<CTC>>(CT cowType,
                                                                                           CTC configuration,
                                                                                           int loadingPriority) {
    public static final Codec<ConfiguredCowType<?, ?>> CODEC = CowType.CODEC.dispatch(ConfiguredCowType::cowType, CowType::getCodec);

}
