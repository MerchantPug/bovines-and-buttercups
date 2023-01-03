package net.merchantpug.bovinesandbuttercups.api.type;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class CowType<CTC extends CowTypeConfiguration> {
    public static final Codec<CowType<?>> CODEC = Services.DATA.getCowTypeCodec();

    private final Codec<ConfiguredCowType<CTC, CowType<CTC>>> configuredCodec;

    public CowType(MapCodec<CTC> codec) {
        this.configuredCodec = codec.xmap((config) -> new ConfiguredCowType<>(this, config), ConfiguredCowType::getConfiguration).codec();
    }

    public Codec<ConfiguredCowType<CTC, CowType<CTC>>> getCodec() {
        return this.configuredCodec;
    }
}
