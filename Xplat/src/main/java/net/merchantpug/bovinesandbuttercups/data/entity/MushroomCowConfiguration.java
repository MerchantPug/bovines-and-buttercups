package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.Optional;

public record MushroomCowConfiguration(MushroomType mushroom,
                                       Optional<ResourceLocation> cowTexture,
                                       ResourceLocation backTexture,
                                       boolean backGrassTinted,
                                       boolean canMakeSuspiciousStew,
                                       Optional<TagKey<Biome>> biomeTagKey,
                                       int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<MushroomCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            MushroomType.CODEC.fieldOf("mushroom").forGetter(MushroomCowConfiguration::mushroom),
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::cowTexture),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png")).forGetter(MushroomCowConfiguration::backTexture),
            Codec.BOOL.optionalFieldOf("back_grass_tinted", false).forGetter(MushroomCowConfiguration::backGrassTinted),
            Codec.BOOL.optionalFieldOf("can_make_suspicious_stew", false).forGetter(MushroomCowConfiguration::canMakeSuspiciousStew),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(MushroomCowConfiguration::naturalSpawnWeight)
    ).apply(builder, MushroomCowConfiguration::new));

    public static final MushroomCowConfiguration MISSING = new MushroomCowConfiguration(MushroomType.MISSING, Optional.of(BovinesAndButtercups.asResource("textures/entity/mooshroom/missing_mooshroom.png")), BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png"), false, false, Optional.empty(), 0);
}
