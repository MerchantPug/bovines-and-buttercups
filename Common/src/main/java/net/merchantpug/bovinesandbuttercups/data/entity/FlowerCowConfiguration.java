package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlowerCowConfiguration extends CowTypeConfiguration {
    private final FlowerCowFlower flower;
    private final FlowerCowFlower bud;
    private final BackGrassConfiguration backGrassConfiguration;
    private final Optional<MobEffectInstance> nectarEffectInstance;
    private final Optional<List<BlockState>> breedingRequirements;

    FlowerCowConfiguration(Optional<ResourceLocation> cowTexture,
                           Optional<HolderSet<Biome>> biomes,
                           int naturalSpawnWeight,
                           Optional<List<WeightedConfiguredCowType>> thunderConverts,
                           FlowerCowFlower flower,
                           FlowerCowFlower bud,
                           BackGrassConfiguration backGrassConfiguration,
                           Optional<MobEffectInstance> nectarEffectInstance,
                           Optional<List<BlockState>> breedingRequirements) {
        super(cowTexture, biomes, naturalSpawnWeight, thunderConverts);
        this.flower = flower;
        this.bud = bud;
        this.backGrassConfiguration = backGrassConfiguration;
        this.nectarEffectInstance = nectarEffectInstance;
        this.breedingRequirements = breedingRequirements;
    }

    public static final Codec<FlowerCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getCowTexture),
            Biome.LIST_CODEC.optionalFieldOf("spawn_biomes").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getBiomes),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(CowTypeConfiguration::getNaturalSpawnWeight),
            Codec.list(WeightedConfiguredCowType.CODEC).optionalFieldOf("thunder_conversion_types").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getThunderConversionTypes),
            FlowerCowFlower.CODEC.fieldOf("flower").forGetter(FlowerCowConfiguration::getFlower),
            FlowerCowFlower.CODEC.fieldOf("bud").forGetter(FlowerCowConfiguration::getBud),
            BackGrassConfiguration.CODEC.optionalFieldOf("back_grass", new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png"), true)).forGetter(FlowerCowConfiguration::getBackGrassConfiguration),
            MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getNectarEffectInstance),
            Codec.list(BlockState.CODEC).optionalFieldOf("breeding_requirements").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getBreedingRequirements)
    ).apply(builder, FlowerCowConfiguration::new));

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(Optional.of(BovinesAndButtercups.asResource("textures/entity/moobloom/missing_moobloom.png")), Optional.empty(), 0, Optional.empty(), new FlowerCowFlower(Optional.empty(), Optional.empty(), Optional.of(BovinesAndButtercups.asResource("missing"))), new FlowerCowFlower(Optional.empty(), Optional.empty(), Optional.of(BovinesAndButtercups.asResource("missing"))), new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png"), true), Optional.empty(), Optional.empty());

    public FlowerCowFlower getFlower() {
        return this.flower;
    }

    public FlowerCowFlower getBud() {
        return this.bud;
    }

    public BackGrassConfiguration getBackGrassConfiguration() {
        return this.backGrassConfiguration;
    }

    public Optional<MobEffectInstance> getNectarEffectInstance() {
        return this.nectarEffectInstance;
    }

    public Optional<List<BlockState>> getBreedingRequirements() {
        return this.breedingRequirements;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowConfiguration other))
            return false;

        return super.equals(obj) && other.flower.equals(this.flower) && other.bud.equals(this.bud) && other.backGrassConfiguration.equals(this.backGrassConfiguration) && other.nectarEffectInstance.equals(this.nectarEffectInstance) && other.breedingRequirements.equals(this.breedingRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getCowTexture(), super.getBiomes(), super.getNaturalSpawnWeight(), this.flower, this.bud, this.backGrassConfiguration, this.nectarEffectInstance, this.breedingRequirements);
    }

    public record FlowerCowFlower(Optional<BlockState> blockState,
                                  Optional<ResourceLocation> modelLocation,
                                  Optional<ResourceLocation> flowerType) {

        public static final Codec<FlowerCowFlower> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(FlowerCowFlower::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").orElseGet(Optional::empty).forGetter(FlowerCowFlower::modelLocation),
                ResourceLocation.CODEC.optionalFieldOf("flower_type").orElseGet(Optional::empty).forGetter(FlowerCowFlower::flowerType)
        ).apply(builder, FlowerCowFlower::new));

        public Optional<FlowerType> getFlowerType(LevelAccessor level) {
            return flowerType.map(resourceLocation -> BovineRegistryUtil.getFlowerTypeFromKey(level, resourceLocation));
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this)
                return true;

            if (!(obj instanceof FlowerCowConfiguration.FlowerCowFlower other))
                return false;

            return other.blockState.equals(this.blockState) && other.modelLocation.equals(this.modelLocation) && other.flowerType.equals(this.flowerType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blockState, this.modelLocation, this.flowerType);
        }
    }
}