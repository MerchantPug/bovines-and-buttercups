package net.merchantpug.bovinesandbuttercups.api.condition;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Predicate;

public class ConditionConfiguration<T> implements Predicate<T> {
    @Override
    public boolean test(T t) {
        return false;
    }

    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {

    }
}
