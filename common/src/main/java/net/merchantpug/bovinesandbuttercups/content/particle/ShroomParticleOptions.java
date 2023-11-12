package net.merchantpug.bovinesandbuttercups.content.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector3f;

import java.util.Locale;

public record ShroomParticleOptions(Vector3f color) implements ParticleOptions {
    public static final Deserializer<ShroomParticleOptions> DESERIALIZER = new Deserializer<>() {
        public ShroomParticleOptions fromCommand(ParticleType<ShroomParticleOptions> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float f = reader.readFloat();
            reader.expect(' ');
            float g = reader.readFloat();
            reader.expect(' ');
            float h = reader.readFloat();
            return new ShroomParticleOptions(new Vector3f(f, g, h));
        }

        public ShroomParticleOptions fromNetwork(ParticleType<ShroomParticleOptions> particleType, FriendlyByteBuf buf) {
            return new ShroomParticleOptions(new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()));
        }
    };

    public static final Codec<ShroomParticleOptions> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ExtraCodecs.VECTOR3F.fieldOf("color").forGetter(ShroomParticleOptions::color)
    ).apply(instance, ShroomParticleOptions::new));

    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeFloat(this.color.x());
        buf.writeFloat(this.color.y());
        buf.writeFloat(this.color.z());
    }

    public String writeToString() {
        return String.format(
                Locale.ROOT, "%s %.2f %.2f %.2f", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()), this.color.x(), this.color.y(), this.color.z()
        );
    }

    public ParticleType<ShroomParticleOptions> getType() {
        return BovineParticleTypes.SHROOM.get();
    }
}
