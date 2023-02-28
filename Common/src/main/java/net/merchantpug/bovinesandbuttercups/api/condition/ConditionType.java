package net.merchantpug.bovinesandbuttercups.api.condition;

import com.mojang.serialization.Codec;

public abstract class ConditionType<T, CC extends ConditionConfiguration<T>> {
    protected Codec<ConfiguredCondition<T, CC, ConditionType<T, CC>>> configuredCodec;

    public Codec<ConfiguredCondition<T, CC, ConditionType<T, CC>>> getCodec() {
        return configuredCodec;
    }
}
