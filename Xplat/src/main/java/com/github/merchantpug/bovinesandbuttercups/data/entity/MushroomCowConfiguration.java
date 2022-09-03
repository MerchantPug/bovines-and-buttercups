package com.github.merchantpug.bovinesandbuttercups.data.entity;

import com.github.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record MushroomCowConfiguration(MushroomType mushroom,
                                      boolean canMakeSuspiciousStew,
                                      Optional<TagKey<Biome>> biomeTagKey,
                                      int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<MushroomCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            MushroomType.CODEC.fieldOf("mushroom").forGetter(MushroomCowConfiguration::mushroom),
            Codec.BOOL.optionalFieldOf("can_make_suspicious_stew", false).forGetter(MushroomCowConfiguration::canMakeSuspiciousStew),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(MushroomCowConfiguration::naturalSpawnWeight)
    ).apply(builder, MushroomCowConfiguration::new));

    public static final MushroomCowConfiguration MISSING = new MushroomCowConfiguration(MushroomType.MISSING, false, Optional.empty(), 0);
}
