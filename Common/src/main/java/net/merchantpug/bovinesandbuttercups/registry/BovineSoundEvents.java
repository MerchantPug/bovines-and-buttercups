package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class BovineSoundEvents {
    public static final RegistrationProvider<SoundEvent> SOUND_EVENTS = RegistrationProvider.get(Registry.SOUND_EVENT, BovinesAndButtercups.MOD_ID);

    public static final RegistryObject<SoundEvent> MOOBLOOM_DRINK = register("entity.moobloom.drink", () -> new SoundEvent(BovinesAndButtercups.asResource("entity.moobloom.drink")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_EAT = register("entity.moobloom.eat", () -> new SoundEvent(BovinesAndButtercups.asResource("entity.moobloom.eat")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_MILK = register("entity.moobloom.milk", () -> new SoundEvent(BovinesAndButtercups.asResource("entity.moobloom.milk")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_SHEAR = register("entity.moobloom.shear", () -> new SoundEvent(BovinesAndButtercups.asResource("entity.moobloom.shear")));
    public static final RegistryObject<SoundEvent> MOOBLOOM_CONVERT = register("entity.moobloom.convert", () -> new SoundEvent(BovinesAndButtercups.asResource("entity.moobloom.convert")));

    public static void init() {

    }

    public static RegistryObject<SoundEvent> register(String soundName, Supplier<SoundEvent> sound) {
        return SOUND_EVENTS.register(soundName, sound);
    }
}
