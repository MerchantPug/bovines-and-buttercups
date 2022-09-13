package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;

public class BovineEffects {
    public static final RegistrationProvider<MobEffect> MOB_EFFECT = RegistrationProvider.get(Registry.MOB_EFFECT, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<MobEffect> LOCKDOWN = MOB_EFFECT.register("lockdown", LockdownEffect::new);

    public static void init() {

    }
}
