package net.merchantpug.bovinesandbuttercups.capabilities;

import com.google.common.collect.ImmutableMap;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;

public interface ILockdownEffectAttachability {
    ResourceLocation ID = BovinesAndButtercups.asResource("lockdown");
    ImmutableMap<MobEffect, Integer> NO_EFFECTS = ImmutableMap.of();

    Map<MobEffect, Integer> getLockdownMobEffects();
    void addLockdownMobEffect(MobEffect effect, int duration);
    void removeLockdownMobEffect(MobEffect effect);
    void setLockdownMobEffects(Map<MobEffect, Integer> map);
}