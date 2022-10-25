package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;

import java.util.function.Supplier;

public class BovineEffects {
    public static final RegistrationProvider<MobEffect> MOB_EFFECTS = RegistrationProvider.get(Registry.MOB_EFFECT, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<MobEffect> LOCKDOWN = register("lockdown", Services.PLATFORM.getLockdownEffectSupplier());

    public static void init() {

    }

    public static RegistryObject<MobEffect> register(String itemName, Supplier<MobEffect> item) {
        return MOB_EFFECTS.register(itemName, item);
    }
}
