package net.merchantpug.bovinesandbuttercups.access;

import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;

public interface ClientboundUpdateMobEffectPacketAccess {
    HashMap<MobEffect, Integer> bovinesandbuttercups$getLockedEffects();
}
