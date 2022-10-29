package net.merchantpug.bovinesandbuttercups.capabilities;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface LockdownEffectCapability extends INBTSerializable<CompoundTag> {
    Capability<LockdownEffectCapabilityImpl> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
    ImmutableMap<MobEffect, Integer> NO_EFFECTS = ImmutableMap.of();

    Map<MobEffect, Integer> getLockdownMobEffects();
    void addLockdownMobEffect(MobEffect effect, int duration);
    void removeLockdownMobEffect(MobEffect effect);
    void setLockdownMobEffects(Map<MobEffect, Integer> map);

    void sync();
}