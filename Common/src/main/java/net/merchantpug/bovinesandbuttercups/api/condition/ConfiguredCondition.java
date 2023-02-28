package net.merchantpug.bovinesandbuttercups.api.condition;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class ConfiguredCondition<T, CC extends ConditionConfiguration<T>, CT extends ConditionType<T, CC>> implements Predicate<T> {
    protected final CT conditionType;
    private final CC conditionConfiguration;

    public ConfiguredCondition(CT conditionType, CC conditionConfiguration) {
        this.conditionType = conditionType;
        this.conditionConfiguration = conditionConfiguration;
    }

    @Override
    public boolean test(T t) {
        return conditionConfiguration.test(t);
    }

    public void returnCowFeedback(LivingEntity entity, ParticleOptions particleOptions) {
        conditionConfiguration.returnCowFeedback(entity, particleOptions);
    }

    public CC getConfiguration() {
        return conditionConfiguration;
    }

    public static <CT extends ConditionType<?, ?>> CT getType(ConfiguredCondition<?, ?, ?> configured) {
        return (CT) configured.conditionType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof ConfiguredCondition<?, ?, ?> other))
            return false;

        return this.conditionType.equals(other.conditionType);
    }
}
