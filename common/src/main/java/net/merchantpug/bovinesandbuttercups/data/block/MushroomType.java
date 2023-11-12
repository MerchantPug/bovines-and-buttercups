package net.merchantpug.bovinesandbuttercups.data.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record MushroomType(
        int loadingPriority,
        Optional<List<ResourceLocation>> hugeMushroomStructureList) {

    public static final MapCodec<MushroomType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.optionalFieldOf("loading_priority", 0).forGetter(MushroomType::loadingPriority),
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("huge_structures").forGetter(MushroomType::hugeMushroomStructureList)
    ).apply(builder, MushroomType::new));

    public static final MushroomType MISSING = new MushroomType(Integer.MAX_VALUE, Optional.empty());

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof MushroomType other))
            return false;

        return other.hugeMushroomStructureList.equals(this.hugeMushroomStructureList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.hugeMushroomStructureList);
    }
}