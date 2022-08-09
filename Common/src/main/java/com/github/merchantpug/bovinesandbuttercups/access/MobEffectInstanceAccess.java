package com.github.merchantpug.bovinesandbuttercups.access;


import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;

public interface MobEffectInstanceAccess {
    HashMap<MobEffect, Integer> bovinesandbuttercups$getLockedEffects();

    void bovinesandbuttercups$setLockedEffects(HashMap<MobEffect, Integer> lockedEffects);

    void bovinesandbuttercups$addLockedEffect(MobEffect statusEffect, int duration);

    void bovinesandbuttercups$removeLockedEffects(MobEffect statusEffect);

    void bovinesandbuttercups$clearLockedEffects();
}
