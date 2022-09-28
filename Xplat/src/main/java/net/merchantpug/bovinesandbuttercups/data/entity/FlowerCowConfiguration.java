package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record FlowerCowConfiguration(FlowerType flower,
                                     FlowerType bud,
                                     Optional<ResourceLocation> cowTexture,
                                     ResourceLocation grassTexture,
                                     boolean backGrassTinted,
                                     Optional<MobEffectInstance> nectarEffectInstance,
                                     Optional<FlowerCowBreedingRequirements> breedingRequirements,
                                     Optional<TagKey<Biome>> biomeTagKey,
                                     int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<FlowerCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            FlowerType.CODEC.fieldOf("flower").forGetter(FlowerCowConfiguration::flower),
            FlowerType.CODEC.fieldOf("bud").forGetter(FlowerCowConfiguration::bud),
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::cowTexture),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png")).forGetter(FlowerCowConfiguration::grassTexture),
            Codec.BOOL.optionalFieldOf("back_grass_tinted", true).forGetter(FlowerCowConfiguration::backGrassTinted),
            MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::nectarEffectInstance),
            FlowerCowBreedingRequirements.CODEC.codec().optionalFieldOf("breeding_requirements").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::breedingRequirements),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(FlowerCowConfiguration::naturalSpawnWeight)
    ).apply(builder, FlowerCowConfiguration::new));

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(FlowerType.MISSING, FlowerType.MISSING, Optional.of(BovinesAndButtercups.asResource("textures/entity/moobloom/missing_moobloom.png")), BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png"), true, Optional.empty(), Optional.empty(), Optional.empty(), 0);
}