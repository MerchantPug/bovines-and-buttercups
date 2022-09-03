package com.github.merchantpug.bovinesandbuttercups.api;

import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class CowType<CTC extends CowTypeConfiguration> {
    public static final Codec<CowType<?>> CODEC = Services.PLATFORM.getCowTypeCodec();

    private final Codec<ConfiguredCowType<CTC, CowType<CTC>>> configuredCodec;

    public CowType(MapCodec<CTC> codec) {
        this.configuredCodec = codec.xmap((config) -> new ConfiguredCowType<>(this, config), ConfiguredCowType::getConfiguration).codec();
    }

    public Codec<ConfiguredCowType<CTC, CowType<CTC>>> getCodec() {
        return this.configuredCodec;
    }
}
