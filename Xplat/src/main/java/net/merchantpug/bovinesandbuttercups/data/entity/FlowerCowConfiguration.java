package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.Optional;

public record FlowerCowConfiguration(Optional<BlockState> flowerBlockState,
                                     Optional<ResourceLocation> flowerModelLocation,
                                     String flowerModelVariant,
                                     Optional<ResourceLocation> customFlower,
                                     Optional<BlockState> budBlockState,
                                     Optional<ResourceLocation> budModelLocation,
                                     String budModelVariant,
                                     Optional<ResourceLocation> customBud,
                                     Optional<ResourceLocation> cowTexture,
                                     ResourceLocation backTexture,
                                     boolean backGrassTinted,
                                     Optional<MobEffectInstance> nectarEffectInstance,
                                     Optional<FlowerCowBreedingRequirements> breedingRequirements,
                                     Optional<TagKey<Biome>> biomeTagKey,
                                     int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<FlowerCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BlockState.CODEC.optionalFieldOf("flower_block_state").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::flowerBlockState),
            ResourceLocation.CODEC.optionalFieldOf("flower_model_location").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::flowerModelLocation),
            Codec.STRING.optionalFieldOf("flower_model_variant", "bovines").forGetter(FlowerCowConfiguration::flowerModelVariant),
            ResourceLocation.CODEC.optionalFieldOf("custom_flower").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::customFlower),
            BlockState.CODEC.optionalFieldOf("bud_block_state").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::budBlockState),
            ResourceLocation.CODEC.optionalFieldOf("bud_model_location").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::budModelLocation),
            Codec.STRING.optionalFieldOf("bud_model_variant", "bovines").forGetter(FlowerCowConfiguration::budModelVariant),
            ResourceLocation.CODEC.optionalFieldOf("custom_bud").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::customBud),
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::cowTexture),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png")).forGetter(FlowerCowConfiguration::backTexture),
            Codec.BOOL.optionalFieldOf("back_grass_tinted", true).forGetter(FlowerCowConfiguration::backGrassTinted),
            MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::nectarEffectInstance),
            FlowerCowBreedingRequirements.CODEC.codec().optionalFieldOf("breeding_requirements").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::breedingRequirements),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(FlowerCowConfiguration::naturalSpawnWeight)
    ).apply(builder, FlowerCowConfiguration::new));

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing")), Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing")), Optional.of(BovinesAndButtercups.asResource("textures/entity/moobloom/missing_moobloom.png")), BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png"), true, Optional.empty(), Optional.empty(), Optional.empty(), 0);

    public Optional<FlowerType> getFlowerType(LevelAccessor level) {
        if (this.customFlower.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(BovineRegistryUtil.getFlowerTypeFromKey(level, this.customFlower.get()));
    }

    public Optional<FlowerType> getBudType(LevelAccessor level) {
        if (this.customBud.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(BovineRegistryUtil.getFlowerTypeFromKey(level, this.customBud.get()));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowConfiguration other))
            return false;

        return other.flowerBlockState.equals(this.flowerBlockState) && other.flowerModelLocation.equals(this.flowerModelLocation) && other.flowerModelVariant.equals(this.flowerModelVariant) && other.customFlower.equals(this.customFlower) && other.budBlockState.equals(this.budBlockState) && other.budModelLocation.equals(this.budModelLocation) && other.budModelVariant.equals(this.budModelVariant) && other.customBud.equals(this.customBud) && other.cowTexture.equals(this.cowTexture) && other.backTexture.equals(this.backTexture) && other.backGrassTinted == this.backGrassTinted && other.nectarEffectInstance.equals(this.nectarEffectInstance) && other.breedingRequirements.equals(this.breedingRequirements) && other.biomeTagKey.equals(this.biomeTagKey) && other.naturalSpawnWeight == this.naturalSpawnWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.flowerBlockState, this.flowerModelLocation, this.flowerModelVariant, this.customFlower, this.budBlockState, this.budModelLocation, this.budModelVariant, this.customBud, this.cowTexture, this.backTexture, this.backGrassTinted, this.nectarEffectInstance, this.breedingRequirements, this.biomeTagKey, this.naturalSpawnWeight);
    }
}