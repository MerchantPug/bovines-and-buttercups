package net.merchantpug.bovinesandbuttercups.api.condition.data.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.List;
import java.util.function.Function;

public class AndConditionConfiguration<T> extends ConditionConfiguration<T> {
    private final List<ConfiguredCondition<T, ?, ?>> conditions;

    public AndConditionConfiguration(List<ConfiguredCondition<T, ?, ?>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(T t) {
        return conditions.stream().allMatch(configuredCondition -> configuredCondition.test(t));
    }

    @Override
    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {
        conditions.forEach(condition -> condition.returnCowFeedback(parent, particle));
    }

    public List<ConfiguredCondition<T, ?, ?>> getConditions() {
        return this.conditions;
    }

    public static <T> Function<RegistryAccess, MapCodec<AndConditionConfiguration<T>>> getCodec(Function<RegistryAccess, Codec<ConfiguredCondition<T, ?, ?>>> configuredConditionCodec) {
        return registryAccess -> RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.list(configuredConditionCodec.apply(registryAccess)).fieldOf("conditions").forGetter(AndConditionConfiguration::getConditions)
        ).apply(builder, AndConditionConfiguration::new));
    }
}
