package com.github.merchantpug.bovinesandbuttercups.data.entity;

import com.github.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import com.github.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record FlowerCowConfiguration(FlowerType flower,
                                     FlowerType bud,
                                     Optional<MobEffectInstance> nectarEffectInstance,
                                     Optional<FlowerCowBreedingRequirements> breedingRequirements,
                                     Optional<TagKey<Biome>> biomeTagKey,
                                     int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<FlowerCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            FlowerType.CODEC.fieldOf("flower").forGetter(FlowerCowConfiguration::flower),
            FlowerType.CODEC.fieldOf("bud").forGetter(FlowerCowConfiguration::bud),
            MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::nectarEffectInstance),
            FlowerCowBreedingRequirements.CODEC.codec().optionalFieldOf("breeding_requirements").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::breedingRequirements),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(FlowerCowConfiguration::naturalSpawnWeight)
    ).apply(builder, FlowerCowConfiguration::new));

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(FlowerType.MISSING, FlowerType.MISSING, Optional.empty(), Optional.empty(), Optional.empty(), 0);
}