package net.merchantpug.bovinesandbuttercups.data.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public record ModelInformation(ResourceLocation location, String variant) {
    public static final Codec<ModelInformation> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.fieldOf("location").forGetter(ModelInformation::location),
            Codec.STRING.optionalFieldOf("variant", "bovines").forGetter(ModelInformation::variant)
    ).apply(builder, ModelInformation::new));

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof ModelInformation other))
            return false;

        return other.location == this.location && other.variant.equals(this.variant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, variant);
    }
}
