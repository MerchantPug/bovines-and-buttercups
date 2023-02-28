package net.merchantpug.bovinesandbuttercups.api.condition.data.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.RegistryAccess;

import java.util.function.Function;

public class NotConditionConfiguration<T> extends ConditionConfiguration<T> {
    private final ConfiguredCondition<T, ?, ?> condition;

    public NotConditionConfiguration(ConfiguredCondition<T, ?, ?> condition) {
        this.condition = condition;
    }

    @Override
    public boolean test(T t) {
        return !condition.test(t);
    }

    public ConfiguredCondition<T, ?, ?> getCondition() {
        return condition;
    }

    public static <T> Function<RegistryAccess, MapCodec<NotConditionConfiguration<T>>> getCodec(Function<RegistryAccess, Codec<ConfiguredCondition<T, ?, ?>>> configuredConditionCodec) {
        return registryAccess -> RecordCodecBuilder.mapCodec(builder -> builder.group(
                configuredConditionCodec.apply(registryAccess).fieldOf("condition").forGetter(NotConditionConfiguration::getCondition)
        ).apply(builder, NotConditionConfiguration::new));
    }
}
