package net.merchantpug.bovinesandbuttercups.api.condition.data.meta;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OrConditionConfiguration<T> extends ConditionConfiguration<T> {
    private final List<ConfiguredCondition<T, ?, ?>> conditions;
    private ConfiguredCondition<T, ?, ?> successfulCondition;

    public OrConditionConfiguration(List<ConfiguredCondition<T, ?, ?>> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean test(T t) {
        Optional<ConfiguredCondition<T, ?, ?>> condition = conditions.stream().filter(configuredCondition -> configuredCondition.test(t)).findFirst();
        if (condition.isPresent()) {
            this.successfulCondition = condition.get();
            return true;
        }
        return false;
    }

    @Override
    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {
        successfulCondition.returnCowFeedback(parent, particle);
        successfulCondition = null;
    }

    public List<ConfiguredCondition<T, ?, ?>> getConditions() {
        return conditions;
    }

    public static <T> Function<RegistryAccess, MapCodec<OrConditionConfiguration<T>>> getCodec(Function<RegistryAccess, Codec<ConfiguredCondition<T, ?, ?>>> configuredConditionCodec) {
        return registryAccess -> RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.list(configuredConditionCodec.apply(registryAccess)).fieldOf("conditions").forGetter(OrConditionConfiguration::getConditions)
        ).apply(builder, OrConditionConfiguration::new));
    }
}
