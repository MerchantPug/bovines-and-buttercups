package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MushroomCowConfiguration extends CowTypeConfiguration {
    private final MushroomCowMushroom mushroom;
    private final ResourceLocation backTexture;
    private final boolean backGrassTinted;
    private final boolean canEatFlowers;

    public MushroomCowConfiguration(Optional<ResourceLocation> cowTexture,
                                    Optional<TagKey<Biome>> biomeTagKey,
                                    int naturalSpawnWeight,
                                    Optional<List<WeightedConfiguredCowType>> thunderConverts,
                                    MushroomCowMushroom mushroom,
                                    ResourceLocation backTexture,
                                    boolean backGrassTinted,
                                    boolean canEatFlowers) {
        super(cowTexture, biomeTagKey, naturalSpawnWeight, thunderConverts);
        this.mushroom = mushroom;
        this.backTexture = backTexture;
        this.backGrassTinted = backGrassTinted;
        this.canEatFlowers = canEatFlowers;
    }

    public static final Codec<MushroomCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::getCowTexture),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::getBiomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(MushroomCowConfiguration::getNaturalSpawnWeight),
            Codec.list(WeightedConfiguredCowType.CODEC).optionalFieldOf("thunder_conversion_types").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getThunderConversionTypes),
            MushroomCowMushroom.CODEC.fieldOf("mushroom").forGetter(MushroomCowConfiguration::getMushroom),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png")).forGetter(MushroomCowConfiguration::getBackTexture),
            Codec.BOOL.optionalFieldOf("tint_back_texture", false).forGetter(MushroomCowConfiguration::isBackGrassTinted),
            Codec.BOOL.optionalFieldOf("can_eat_flowers", false).forGetter(MushroomCowConfiguration::canEatFlowers)
    ).apply(builder, MushroomCowConfiguration::new));

    public static final MushroomCowConfiguration MISSING = new MushroomCowConfiguration(Optional.of(BovinesAndButtercups.asResource("textures/entity/mooshroom/missing_mooshroom.png")),Optional.empty(), 0, Optional.empty(), new MushroomCowMushroom(Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing"))), BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png"), false, false);

    public MushroomCowConfiguration.MushroomCowMushroom getMushroom() {
        return this.mushroom;
    }

    public ResourceLocation getBackTexture() {
        return this.backTexture;
    }

    public boolean isBackGrassTinted() {
        return this.backGrassTinted;
    }

    public boolean canEatFlowers() {
        return this.canEatFlowers;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof MushroomCowConfiguration other))
            return false;

        return super.equals(obj) && other.mushroom.equals(this.mushroom) && other.backTexture.equals(this.backTexture) && other.backGrassTinted == this.backGrassTinted && other.canEatFlowers == this.canEatFlowers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.getCowTexture(), super.getBiomeTagKey(), super.getNaturalSpawnWeight(), this.mushroom, this.backTexture, this.backGrassTinted, this.canEatFlowers);
    }

    public record MushroomCowMushroom(Optional<BlockState> blockState,
                                  Optional<ResourceLocation> modelLocation,
                                  String modelVariant,
                                  Optional<ResourceLocation> mushroomType) {

        public static final Codec<MushroomCowMushroom> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration.MushroomCowMushroom::blockState),
                ResourceLocation.CODEC.optionalFieldOf("model_location").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration.MushroomCowMushroom::modelLocation),
                Codec.STRING.optionalFieldOf("model_variant", "bovines").forGetter(MushroomCowConfiguration.MushroomCowMushroom::modelVariant),
                ResourceLocation.CODEC.optionalFieldOf("mushroom_type").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration.MushroomCowMushroom::mushroomType)
        ).apply(builder, MushroomCowConfiguration.MushroomCowMushroom::new));

        public Optional<MushroomType> getMushroomType(LevelAccessor level) {
            return mushroomType.map(resourceLocation -> BovineRegistryUtil.getMushroomTypeFromKey(level, resourceLocation));
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this)
                return true;

            if (!(obj instanceof MushroomCowMushroom other))
                return false;

            return other.blockState.equals(this.blockState) && other.modelLocation.equals(this.modelLocation) && other.modelVariant.equals(this.modelVariant) && other.mushroomType.equals(this.mushroomType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.blockState, this.modelLocation, this.modelVariant, this.mushroomType);
        }
    }
}
