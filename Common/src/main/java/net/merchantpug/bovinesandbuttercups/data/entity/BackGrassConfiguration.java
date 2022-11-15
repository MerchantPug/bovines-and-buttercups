package net.merchantpug.bovinesandbuttercups.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record BackGrassConfiguration(ResourceLocation textureLocation, boolean grassTinted) {
    public static final Codec<BackGrassConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("texture_location").forGetter(BackGrassConfiguration::textureLocation),
            Codec.BOOL.fieldOf("grass_tinted").forGetter(BackGrassConfiguration::grassTinted)
    ).apply(builder, BackGrassConfiguration::new));

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof BackGrassConfiguration other))
            return false;

        return other.textureLocation.equals(this.textureLocation) && other.grassTinted == this.grassTinted;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.textureLocation, this.grassTinted);
    }
}
