package com.github.merchantpug.bovinesandbuttercups.particle;

import com.github.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ModelLocationParticleOption implements ParticleOptions {
    public static final ParticleOptions.Deserializer<ModelLocationParticleOption> DESERIALIZER = new ParticleOptions.Deserializer<>() {
        public ModelLocationParticleOption fromCommand(ParticleType<ModelLocationParticleOption> particleType, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new ModelLocationParticleOption(ResourceLocation.tryParse(reader.getString()), reader.getString());
        }

        public ModelLocationParticleOption fromNetwork(ParticleType<ModelLocationParticleOption> particleType, FriendlyByteBuf buf) {
            return new ModelLocationParticleOption(buf.readResourceLocation(), buf.readUtf());
        }
    };

    private final ResourceLocation modelKey;
    private final String modelVariant;

    public static final Codec<ModelLocationParticleOption> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(ResourceLocation.CODEC.fieldOf("key").forGetter((resourceLocation) -> {
            return resourceLocation.modelKey;
        }), Codec.STRING.fieldOf("variant").forGetter((string) -> {
            return string.modelVariant;
        })).apply(instance, ModelLocationParticleOption::new);
    });

    public ModelLocationParticleOption(ResourceLocation modelKey, String modelVariant) {
        this.modelKey = modelKey;
        this.modelVariant = modelVariant;
    }

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

    public ResourceLocation getModelKey() {
        return this.modelKey;
    }

    public String getModelVariant() {
        return this.modelVariant;
    }
}
