package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.util.Color;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.Objects;
import java.util.Optional;

public class MushroomCowConfiguration extends CowTypeConfiguration {
    private final MushroomCowMushroom mushroom;
    private final Optional<Vector3f> color;
    private final BackGrassConfiguration backGrassConfiguration;
    private final boolean canEatFlowers;
    private final boolean vanillaSpawningHack;
    private final Optional<BreedingConditionConfiguration> breedingConditions;

    public MushroomCowConfiguration(Settings settings,
                                    MushroomCowMushroom mushroom,
                                    Optional<Vector3f> color,
                                    BackGrassConfiguration backGrassConfiguration,
                                    boolean canEatFlowers,
                                    boolean vanillaSpawningHack,
                                    Optional<BreedingConditionConfiguration> breedingConditions) {
        super(settings);
        this.mushroom = mushroom;
        this.color = color;
        this.backGrassConfiguration = backGrassConfiguration;
        this.canEatFlowers = canEatFlowers;
        this.vanillaSpawningHack = vanillaSpawningHack;
        this.breedingConditions = breedingConditions;
    }

    public static Codec<MushroomCowConfiguration> getCodec(RegistryAccess frozen) {
        return RecordCodecBuilder.create(builder -> builder.group(
                Settings.getCodec(frozen).forGetter(MushroomCowConfiguration::getSettings),
                MushroomCowMushroom.CODEC.fieldOf("mushroom").forGetter(MushroomCowConfiguration::getMushroom),
                Color.CODEC.optionalFieldOf("color").forGetter(MushroomCowConfiguration::getColor),
                BackGrassConfiguration.CODEC.optionalFieldOf("back_grass", new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/mooshroom/mooshroom_mycelium.png"), false)).forGetter(MushroomCowConfiguration::getBackGrassConfiguration),
                Codec.BOOL.optionalFieldOf("can_eat_flowers", false).forGetter(MushroomCowConfiguration::canEatFlowers),
                Codec.BOOL.optionalFieldOf("vanilla_spawning_hack", false).forGetter(MushroomCowConfiguration::usesVanillaSpawningHack),
                BreedingConditionConfiguration.CODEC.optionalFieldOf("breeding_conditions").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::getBreedingConditions)
        ).apply(builder, MushroomCowConfiguration::new));
    }

    public static final MushroomCowConfiguration MISSING = new MushroomCowConfiguration(new Settings(Optional.of(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/mooshroom/missing_mooshroom.png")), Optional.empty(), 0, Optional.empty()), new MushroomCowMushroom(Optional.empty(), Optional.empty(), Optional.of(BovinesAndButtercups.asResource("missing_mushroom"))), Optional.of(new Vector3f(1.0F, 1.0F, 1.0F)), new BackGrassConfiguration(BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/mooshroom/mooshroom_mycelium.png"), false), false, false, Optional.empty());

    public MushroomCowConfiguration.MushroomCowMushroom getMushroom() {
        return this.mushroom;
    }

    public BackGrassConfiguration getBackGrassConfiguration() {
        return this.backGrassConfiguration;
    }

    public boolean canEatFlowers() {
        return this.canEatFlowers;
    }

    public boolean usesVanillaSpawningHack() {
        return this.vanillaSpawningHack;
    }

    public Optional<BreedingConditionConfiguration> getBreedingConditions() {
        return this.breedingConditions;
    }

    public Optional<Vector3f> getColor() {
        return this.color;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof MushroomCowConfiguration other))
            return false;

        return super.equals(obj) && other.mushroom.equals(this.mushroom) && other.backGrassConfiguration.equals(this.backGrassConfiguration) && other.canEatFlowers == this.canEatFlowers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getSettings(), this.mushroom, this.backGrassConfiguration, this.canEatFlowers);
    }

    public record MushroomCowMushroom(Optional<BlockState> blockState,
                                  Optional<ResourceLocation> modelLocation,
                                  Optional<ResourceLocation> mushroomType) {

        public static final Codec<MushroomCowMushroom> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").forGetter(MushroomCowConfiguration.MushroomCowMushroom::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").forGetter(MushroomCowConfiguration.MushroomCowMushroom::modelLocation),
                ResourceLocation.CODEC.optionalFieldOf("mushroom_type").forGetter(MushroomCowConfiguration.MushroomCowMushroom::mushroomType)
        ).apply(builder, MushroomCowConfiguration.MushroomCowMushroom::new));

        public Optional<MushroomType> getMushroomType() {
            return mushroomType.map(BovineRegistryUtil::getMushroomTypeFromKey);
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this)
                return true;

            if (!(obj instanceof MushroomCowMushroom other))
                return false;

            return other.blockState.equals(this.blockState) && other.modelLocation.equals(this.modelLocation) && other.mushroomType.equals(this.mushroomType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blockState, this.modelLocation, this.mushroomType);
        }
    }
}
