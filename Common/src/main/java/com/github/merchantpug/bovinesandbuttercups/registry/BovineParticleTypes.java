package com.github.merchantpug.bovinesandbuttercups.registry;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.particle.ModelLocationParticleOption;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

public class BovineParticleTypes {
    public static final RegistrationProvider<ParticleType<?>> PARTICLE_TYPES = RegistrationProvider.get(Registry.PARTICLE_TYPE, Constants.MOD_ID);

    public static final RegistryObject<ParticleType<ModelLocationParticleOption>> MODEL_LOCATION = register("model_location", false, ModelLocationParticleOption.DESERIALIZER, (particleType) -> {
        return ModelLocationParticleOption.CODEC;
    });

    public static void init() {

    }

    public static <T extends ParticleOptions> RegistryObject<ParticleType<T>> register(String itemName, boolean alwaysShow, ParticleOptions.Deserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> codec) {
        return PARTICLE_TYPES.register(itemName, () -> new ParticleType<>(alwaysShow, deserializer) {
            public Codec<T> codec() { return codec.apply(this); }
        });
    }
}
