package net.merchantpug.bovinesandbuttercups.api;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a generic class with variables that you might want
 * to use for your own cow type configurations.
 */
public class CowTypeConfiguration {
    private final Optional<ResourceLocation> cowTexture;
    private final Optional<TagKey<Biome>> biomeTagKey;
    private final int naturalSpawnWeight;
    private final Optional<List<WeightedConfiguredCowType>> thunderConversionTypes;

    protected CowTypeConfiguration(Optional<ResourceLocation> cowTexture, Optional<TagKey<Biome>> biomeTagKey, int naturalSpawnWeight, Optional<List<WeightedConfiguredCowType>> thunderConverts) {
        this.cowTexture = cowTexture;
        this.biomeTagKey = biomeTagKey;
        this.naturalSpawnWeight = naturalSpawnWeight;
        this.thunderConversionTypes = thunderConverts;
    }

    /**
     * A starting codec for your own custom cow types. This initialises the important fields in this class.
     * Unfortunately due to codec limitations, this method with a Codec.and() only supports 8 individual fields.
     * If you wish to have more fields, use RecordCodecBuilder's creation methods.
     *
     * @param instance The RecordCodecBuilder for your cow type instance.
     * @return A RecordCodecBuilder with the fields in this class.
     * @param <T> The class of your codec that extends CowTypeConfiguration.
     */
    protected static <T extends CowTypeConfiguration> Products.P3<RecordCodecBuilder.Mu<T>, Optional<ResourceLocation>, Optional<TagKey<Biome>>, Integer> codecStart(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
                ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getCowTexture),
                TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("spawn_biome_tag").orElseGet(Optional::empty).forGetter(CowTypeConfiguration::getBiomeTagKey),
                Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(CowTypeConfiguration::getNaturalSpawnWeight));
    }

    public Optional<ResourceLocation> getCowTexture() {
        return this.cowTexture;
    }

    public Optional<TagKey<Biome>> getBiomeTagKey() {
        return this.biomeTagKey;
    }

    public int getNaturalSpawnWeight() {
        return this.naturalSpawnWeight;
    }

    public Optional<List<WeightedConfiguredCowType>> getThunderConversionTypes() {
        return this.thunderConversionTypes;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerCowConfiguration other))
            return false;

        return this.getCowTexture() == other.getCowTexture() && this.getBiomeTagKey() == other.getBiomeTagKey() && this.getNaturalSpawnWeight() == other.getNaturalSpawnWeight();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cowTexture, this.biomeTagKey, this.naturalSpawnWeight);
    }

    public record WeightedConfiguredCowType(ResourceLocation configuredCowType,
                                            int weight) {

        public static final Codec<WeightedConfiguredCowType> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("type").forGetter(WeightedConfiguredCowType::configuredCowType),
                Codec.INT.optionalFieldOf("weight", 1).forGetter(WeightedConfiguredCowType::weight)
        ).apply(builder, WeightedConfiguredCowType::new));


        public Optional<ConfiguredCowType<?, ?>> getConfiguredCowType(LevelAccessor level) {
            if (!BovineRegistryUtil.isConfiguredCowTypeInRegistry(level, configuredCowType)) {
                return Optional.empty();
            }
            return Optional.ofNullable(BovineRegistryUtil.getConfiguredCowTypeFromKey(level, configuredCowType));
        }
    }
}