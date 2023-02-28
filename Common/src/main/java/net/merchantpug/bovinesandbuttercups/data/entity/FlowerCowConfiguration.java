package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.util.Color;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;

public class FlowerCowConfiguration extends CowTypeConfiguration {
    private final FlowerCowFlower flower;
    private final FlowerCowFlower bud;
    private final Vector3f color;
    private final BackGrassConfiguration backGrassConfiguration;
    private final Optional<ResourceLocation> nectarTexture;
    private final Optional<MobEffectInstance> nectarEffectInstance;
    private final Optional<BreedingConditionConfiguration> breedingConditions;

    FlowerCowConfiguration(Settings settings,
                           FlowerCowFlower flower,
                           FlowerCowFlower bud,
                           Vector3f color,
                           BackGrassConfiguration backGrassConfiguration,
                           Optional<ResourceLocation> nectarTexture,
                           Optional<MobEffectInstance> nectarEffectInstance,
                           Optional<BreedingConditionConfiguration> breedingConditions) {
        super(settings);
        this.flower = flower;
        this.bud = bud;
        this.color = color;
        this.backGrassConfiguration = backGrassConfiguration;
        this.nectarTexture = nectarTexture;
        this.nectarEffectInstance = nectarEffectInstance;
        this.breedingConditions = breedingConditions;
    }

    public static Codec<FlowerCowConfiguration> getCodec(RegistryAccess frozen) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Settings.getCodec(frozen).forGetter(FlowerCowConfiguration::getSettings),
                FlowerCowFlower.CODEC.fieldOf("flower").forGetter(FlowerCowConfiguration::getFlower),
                FlowerCowFlower.CODEC.fieldOf("bud").forGetter(FlowerCowConfiguration::getBud),
                Color.CODEC.fieldOf("color").forGetter(FlowerCowConfiguration::getColor),
                BackGrassConfiguration.CODEC.optionalFieldOf("back_grass", new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/moobloom_grass.png"), true)).forGetter(FlowerCowConfiguration::getBackGrassConfiguration),
                ResourceLocation.CODEC.optionalFieldOf("nectar_texture").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getNectarTexture),
                MobEffectUtil.CODEC.optionalFieldOf("nectar_effect").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getNectarEffectInstance),
                BreedingConditionConfiguration.getCodec(frozen).optionalFieldOf("breeding_conditions").orElseGet(Optional::empty).forGetter(FlowerCowConfiguration::getBreedingConditions)
        ).apply(builder, FlowerCowConfiguration::new));
    }

    public static final FlowerCowConfiguration MISSING = new FlowerCowConfiguration(new Settings(Optional.of(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/missing_moobloom.png")), Optional.empty(), 0, Optional.empty()), new FlowerCowFlower(Optional.empty(), Optional.empty(), Optional.of(BovinesAndButtercups.asResource("missing_flower"))), new FlowerCowFlower(Optional.empty(), Optional.empty(), Optional.of(BovinesAndButtercups.asResource("missing"))), new Vector3f(1.0F, 1.0F, 1.0F), new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/moobloom/moobloom_grass.png"), true), Optional.empty(), Optional.empty(), Optional.empty());

    public FlowerCowFlower getFlower() {
        return this.flower;
    }

    public FlowerCowFlower getBud() {
        return this.bud;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public Optional<ResourceLocation> getNectarTexture() {
        return this.nectarTexture;
    }

    public BackGrassConfiguration getBackGrassConfiguration() {
        return this.backGrassConfiguration;
    }

    public Optional<MobEffectInstance> getNectarEffectInstance() {
        return this.nectarEffectInstance;
    }

    public Optional<BreedingConditionConfiguration> getBreedingConditions() {
        return this.breedingConditions;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowConfiguration other))
            return false;

        return super.equals(obj) && other.flower.equals(this.flower) && other.bud.equals(this.bud) && other.backGrassConfiguration.equals(this.backGrassConfiguration) && other.nectarEffectInstance.equals(this.nectarEffectInstance) && other.breedingConditions.equals(this.breedingConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getSettings(), this.flower, this.bud, this.backGrassConfiguration, this.nectarEffectInstance, this.breedingConditions);
    }

    public record FlowerCowFlower(Optional<BlockState> blockState,
                                  Optional<ResourceLocation> modelLocation,
                                  Optional<ResourceLocation> flowerType) {

        public static final Codec<FlowerCowFlower> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").forGetter(FlowerCowFlower::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").forGetter(FlowerCowFlower::modelLocation),
                ResourceLocation.CODEC.optionalFieldOf("flower_type").forGetter(FlowerCowFlower::flowerType)
        ).apply(builder, FlowerCowFlower::new));

        public Optional<FlowerType> getFlowerType() {
            return flowerType.map(BovineRegistryUtil::getFlowerTypeFromKey);
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