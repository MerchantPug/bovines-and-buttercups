package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class BovineSoundEvents {
    public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(Registry.SOUND_EVENT, Constants.MOD_ID);

    public static final RegistryObject<SoundEvent> MOOBLOOM_DRINK = register("entity.moobloom.drink", () -> new SoundEvent(Constants.resourceLocation("entity.moobloom.drink")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_EAT = register("entity.moobloom.eat", () -> new SoundEvent(Constants.resourceLocation("entity.moobloom.eat")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_MILK = register("entity.moobloom.milk", () -> new SoundEvent(Constants.resourceLocation("entity.moobloom.milk")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_SHEAR = register("entity.moobloom.shear", () -> new SoundEvent(Constants.resourceLocation("entity.moobloom.shear")));

    public static void init() {

    }

    public static RegistryObject<SoundEvent> register(String soundName, Supplier<SoundEvent> sound) {
        return SOUND_EVENTS.register(soundName, sound);
    }
}
