package net.merchantpug.bovinesandbuttercups.api.type;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.util.CodecUtil;
import net.merchantpug.bovinesandbuttercups.util.HolderUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This is a generic class with variables that you might want
 * to use for your own cow type configurations.
 */
public class CowTypeConfiguration {
    protected final Settings settings;

    protected CowTypeConfiguration(Settings settings) {
        this.settings = settings;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof CowTypeConfiguration other))
            return false;

        return this.getSettings().equals(other.getSettings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSettings());
    }

    /**
     * @param cowTexture A {@link ResourceLocation} for where in the assets the cow's texture is located, if not set, it'll default to a hardcoded value depending on the cow.
     * @param biomes Either a biome or a biome tag where the cow can spawn.
     * @param naturalSpawnWeight The natural spawn weight for this cow in relation to other cows of its type. Any value below 1 should be ignored.
     * @param thunderConverts A list of weighted cow types that this cow will/have a chance to convert into upon being struck by lightning.
     */
    public record Settings(Optional<ResourceLocation> cowTexture, Optional<HolderUtil.PsuedoHolder<Biome>> biomes, int naturalSpawnWeight, Optional<List<WeightedConfiguredCowType>> thunderConverts) {
        public static MapCodec<Settings> getCodec() {
            return RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(Settings::cowTexture),
                    CodecUtil.optionalTagOrObjectCodec(Registries.BIOME, "spawn_biomes").forGetter(Settings::biomes),
                    Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(Settings::naturalSpawnWeight),
                    Codec.list(WeightedConfiguredCowType.CODEC).optionalFieldOf("thunder_conversion_types").orElseGet(Optional::empty).forGetter(Settings::thunderConverts)
            ).apply(instance, Settings::new));
        }
    }

    public record WeightedConfiguredCowType(ResourceLocation configuredCowTypeResource,
                                            int weight) {
        public static final Codec<WeightedConfiguredCowType> DIRECT_CODEC = RecordCodecBuilder.create(builder -> builder.group(
                ResourceLocation.CODEC.fieldOf("type").forGetter(WeightedConfiguredCowType::configuredCowTypeResource),
                Codec.INT.optionalFieldOf("weight", 1).forGetter(WeightedConfiguredCowType::weight)
        ).apply(builder, WeightedConfiguredCowType::new));

        public static final Codec<WeightedConfiguredCowType> CODEC = Codec.either(DIRECT_CODEC, ResourceLocation.CODEC)
                .xmap(objectResourceLocationEither -> objectResourceLocationEither.map(o -> o, location -> new WeightedConfiguredCowType(location, 1)), Either::left);


        public Optional<ConfiguredCowType<?, ?>> getConfiguredCowType() {
            if (!BovineRegistryUtil.isConfiguredCowTypeInRegistry(configuredCowTypeResource())) {
                return Optional.empty();
            }
            return Optional.ofNullable(BovineRegistryUtil.getConfiguredCowTypeFromKey(configuredCowTypeResource()));
        }
    }
}
