package net.merchantpug.bovinesandbuttercups.api.condition.data.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

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
        return conditions;
    }

    public static <T> MapCodec<AndConditionConfiguration<T>> getCodec(Codec<ConfiguredCondition<T, ?, ?>> configuredConditionCodec) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.list(configuredConditionCodec).fieldOf("conditions").forGetter(AndConditionConfiguration::getConditions)
        ).apply(builder, AndConditionConfiguration::new));
    }
}
