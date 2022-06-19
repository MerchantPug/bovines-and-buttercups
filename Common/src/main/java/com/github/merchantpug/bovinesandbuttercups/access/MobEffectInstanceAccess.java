package com.github.merchantpug.bovinesandbuttercups.access;


import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;

public interface MobEffectInstanceAccess {
    HashMap<MobEffect, Integer> bovinesandbuttercups$getNullifiedEffects();

    void bovinesandbuttercups$setNullifiedEffects(HashMap<MobEffect, Integer> nullifiedEffects);

    void bovinesandbuttercups$addNullifiedEffect(MobEffect statusEffect, int duration);

    void bovinesandbuttercups$removeNullifiedEffects(MobEffect statusEffect);

    void bovinesandbuttercups$clearNullifiedEffects();
}
