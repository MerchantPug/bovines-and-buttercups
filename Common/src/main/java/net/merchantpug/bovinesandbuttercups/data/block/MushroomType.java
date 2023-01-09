package net.merchantpug.bovinesandbuttercups.data.block;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.registry.BovineRegistryKeys;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record MushroomType(Optional<List<ResourceLocation>> hugeMushroomStructureList) {

    public static final MapCodec<MushroomType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.list(ResourceLocation.CODEC).optionalFieldOf("huge_structures").forGetter(MushroomType::hugeMushroomStructureList)
    ).apply(builder, MushroomType::new));

    public static final MushroomType MISSING = new MushroomType(Optional.empty());

    public static Holder<MushroomType> bootstrap(Registry<MushroomType> registry) {
        return BuiltinRegistries.register(registry, ResourceKey.create(BovineRegistryKeys.MUSHROOM_TYPE_KEY, BovinesAndButtercups.asResource("missing_mushroom")), MushroomType.MISSING);
    }

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