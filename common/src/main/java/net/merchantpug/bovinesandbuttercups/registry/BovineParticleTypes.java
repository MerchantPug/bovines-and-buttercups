package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.particle.ModelLocationParticleOption;
import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.content.particle.BloomParticleOptions;
import net.merchantpug.bovinesandbuttercups.content.particle.ShroomParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;

import java.util.function.Function;
import java.util.function.Supplier;

public class BovineParticleTypes {
    private static final RegistrationProvider<ParticleType<?>> PARTICLE_TYPES = RegistrationProvider.get(Registries.PARTICLE_TYPE, BovinesAndButtercups.MOD_ID);

    public static final Supplier<ParticleType<ModelLocationParticleOption>> MODEL_LOCATION = register("model_location", false, ModelLocationParticleOption.DESERIALIZER, (particleType) -> ModelLocationParticleOption.CODEC);
    public static final Supplier<ParticleType<BloomParticleOptions>> BLOOM = register("bloom", false, BloomParticleOptions.DESERIALIZER, (particleType) -> BloomParticleOptions.CODEC);
    public static final Supplier<ParticleType<ShroomParticleOptions>> SHROOM = register("shroom", false, ShroomParticleOptions.DESERIALIZER, (particleType) -> ShroomParticleOptions.CODEC);

    public static void register() {

    }

    private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String name, boolean alwaysShow, ParticleOptions.Deserializer<T> deserializer, final Function<ParticleType<T>, Codec<T>> codec) {
        return PARTICLE_TYPES.register(name, () -> new ParticleType<>(alwaysShow, deserializer) {
            public Codec<T> codec() { return codec.apply(this); }
        });
    }
}
