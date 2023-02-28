package net.merchantpug.bovinesandbuttercups.api.condition.data.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;

public class ConstantConditionConfiguration<T> extends ConditionConfiguration<T> {
    private final boolean value;

    public ConstantConditionConfiguration(boolean value) {
        this.value = value;
    }

    @Override
    public boolean test(T t) {
        return value;
    }

    public boolean getValue() {
        return value;
    }

    public static <T> MapCodec<ConstantConditionConfiguration<T>> getCodec(Codec<ConfiguredCondition<T, ?, ?>> configuredConditionCodec) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.BOOL.fieldOf("value").forGetter(ConstantConditionConfiguration::getValue)
        ).apply(builder, ConstantConditionConfiguration::new));
    }
}
