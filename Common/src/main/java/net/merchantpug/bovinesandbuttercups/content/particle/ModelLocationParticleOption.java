package net.merchantpug.bovinesandbuttercups.content.particle;

import net.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ModelLocationParticleOption(ResourceLocation modelKey, String modelVariant) implements ParticleOptions {
    public static final Deserializer<ModelLocationParticleOption> DESERIALIZER = new Deserializer<>() {
        public ModelLocationParticleOption fromCommand(ParticleType<ModelLocationParticleOption> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ModelLocationParticleOption(ResourceLocation.tryParse(reader.getString()), reader.getString());
        }

        public ModelLocationParticleOption fromNetwork(ParticleType<ModelLocationParticleOption> particleType, FriendlyByteBuf buf) {
            return new ModelLocationParticleOption(buf.readResourceLocation(), buf.readUtf());
        }
    };

    public static final Codec<ModelLocationParticleOption> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            ResourceLocation.CODEC.fieldOf("key").forGetter((resourceLocation) -> resourceLocation.modelKey),
            Codec.STRING.fieldOf("variant").forGetter((string) -> string.modelVariant)
    ).apply(instance, ModelLocationParticleOption::new));

    public void writeToNetwork(FriendlyByteBuf $$0) {
        $$0.writeResourceLocation(this.modelKey);
        $$0.writeUtf(this.modelVariant);
    }

    public String writeToString() {
        ResourceLocation var10000 = Registry.PARTICLE_TYPE.getKey(this.getType());
        return "" + var10000 + " " + modelKey + "/" + modelVariant;
    }

    public ParticleType<ModelLocationParticleOption> getType() {
        return BovineParticleTypes.MODEL_LOCATION.get();
    }
}
