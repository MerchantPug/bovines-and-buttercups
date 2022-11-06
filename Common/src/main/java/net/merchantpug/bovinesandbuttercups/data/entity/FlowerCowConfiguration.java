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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class FlowerCowConfiguration extends CowTypeConfiguration {
    private final FlowerCowFlower flower;
    private final FlowerCowFlower bud;
    private final ResourceLocation backTexture;
    private final boolean backGrassTinted;
    private final Optional<MobEffectInstance> nectarEffectInstance;
    private final Optional<List<BlockState>> breedingRequirements;

    FlowerCowConfiguration(Optional<ResourceLocation> cowTexture,
                           Optional<TagKey<Biome>> biomeTagKey,
                           int naturalSpawnWeight,
                           Optional<List<WeightedConfiguredCowType>> thunderConverts,
                           FlowerCowFlower flower,
                           FlowerCowFlower bud,
                           ResourceLocation backTexture,
                           boolean backGrassTinted,
                           Optional<MobEffectInstance> nectarEffectInstance,
                           Optional<List<BlockState>> breedingRequirements) {
        super(cowTexture, biomeTagKey, naturalSpawnWeight, thunderConverts);
        this.flower = flower;
        this.bud = bud;
        this.backTexture = backTexture;
        this.backGrassTinted = backGrassTinted;
        this.nectarEffectInstance = nectarEffectInstance;
        this.breedingRequirements = breedingRequirements;
    }

    public static final Codec<FlowerCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getCowTexture),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getBiomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(CowTypeConfiguration::getNaturalSpawnWeight),
            Codec.list(WeightedConfiguredCowType.CODEC).optionalFieldOf("thunder_conversion_types").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getThunderConversionTypes),
            FlowerCowFlower.CODEC.fieldOf("flower").forGetter(FlowerCowConfiguration::getFlower),
            FlowerCowFlower.CODEC.fieldOf("bud").forGetter(FlowerCowConfiguration::getBud),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png")).forGetter(FlowerCowConfiguration::getBackTexture),
            Codec.BOOL.optionalFieldOf("tint_back_texture", true).forGetter(FlowerCowConfiguration::isBackGrassTinted),
            MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getNectarEffectInstance),
            Codec.list(BlockState.CODEC).optionalFieldOf("breeding_requirements").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getBreedingRequirements)
    ).apply(builder, FlowerCowConfiguration::new));

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(Optional.of(BovinesAndButtercups.asResource("textures/entity/moobloom/missing_moobloom.png")), Optional.empty(), 0, Optional.empty(), new FlowerCowFlower(Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing"))), new FlowerCowFlower(Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing"))), BovinesAndButtercups.asResource("textures/entity/moobloom/moobloom_grass.png"), true, Optional.empty(), Optional.empty());

    public FlowerCowFlower getFlower() {
        return this.flower;
    }

    public FlowerCowFlower getBud() {
        return this.bud;
    }

    public ResourceLocation getBackTexture() {
        return this.backTexture;
    }

    public boolean isBackGrassTinted() {
        return this.backGrassTinted;
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

        return super.equals(obj) && other.flower.equals(this.flower) && other.bud.equals(this.bud) && other.backTexture.equals(this.backTexture) && other.backGrassTinted == this.backGrassTinted && other.nectarEffectInstance.equals(this.nectarEffectInstance) && other.breedingRequirements.equals(this.breedingRequirements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getCowTexture(), super.getBiomeTagKey(), super.getNaturalSpawnWeight(), this.flower, this.bud, this.backTexture, this.backGrassTinted, this.nectarEffectInstance, this.breedingRequirements);
    }

    public record FlowerCowFlower(Optional<BlockState> blockState,
                                  Optional<ResourceLocation> modelLocation,
                                  String modelVariant,
                                  Optional<ResourceLocation> flowerType) {

        public static final Codec<FlowerCowFlower> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(FlowerCowFlower::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").orElseGet(Optional::empty).forGetter(FlowerCowFlower::modelLocation),
                Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(FlowerCowFlower::modelVariant),
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

            return other.blockState.equals(this.blockState) && other.modelLocation.equals(this.modelLocation) && other.modelVariant.equals(this.modelVariant) && other.flowerType.equals(this.flowerType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blockState, this.modelLocation, this.modelVariant, this.flowerType);
        }
    }
}