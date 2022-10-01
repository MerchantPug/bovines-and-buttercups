package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

public record MushroomCowConfiguration(Optional<BlockState> blockState,
                                       Optional<ResourceLocation> blockModelLocation,
                                       String blockModelVariant,
                                       Optional<ResourceLocation> customMushroom,
                                       Optional<ResourceLocation> cowTexture,
                                       ResourceLocation backTexture,
                                       boolean backGrassTinted,
                                       boolean canMakeSuspiciousStew,
                                       Optional<TagKey<Biome>> biomeTagKey,
                                       int naturalSpawnWeight) implements CowTypeConfiguration {

    public static final Codec<MushroomCowConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            BlockState.CODEC.optionalFieldOf("block_state").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::blockState),
            ResourceLocation.CODEC.optionalFieldOf("block_model_location").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::blockModelLocation),
            Codec.STRING.optionalFieldOf("block_model_variant", "bovines").forGetter(MushroomCowConfiguration::blockModelVariant),
            ResourceLocation.CODEC.optionalFieldOf("custom_mushroom").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::customMushroom),
            ResourceLocation.CODEC.optionalFieldOf("texture_location").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::cowTexture),
            ResourceLocation.CODEC.optionalFieldOf("back_texture_location", BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png")).forGetter(MushroomCowConfiguration::backTexture),
            Codec.BOOL.optionalFieldOf("back_grass_tinted", false).forGetter(MushroomCowConfiguration::backGrassTinted),
            Codec.BOOL.optionalFieldOf("can_make_suspicious_stew", false).forGetter(MushroomCowConfiguration::canMakeSuspiciousStew),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag").orElseGet(Optional::empty).forGetter(MushroomCowConfiguration::biomeTagKey),
            Codec.INT.optionalFieldOf("natural_spawn_weight", 0).forGetter(MushroomCowConfiguration::naturalSpawnWeight)
    ).apply(builder, MushroomCowConfiguration::new));

    public static final MushroomCowConfiguration MISSING = new MushroomCowConfiguration(Optional.empty(), Optional.empty(), "bovines", Optional.of(BovinesAndButtercups.asResource("missing")), Optional.of(BovinesAndButtercups.asResource("textures/entity/mooshroom/missing_mooshroom.png")), BovinesAndButtercups.asResource("textures/entity/mooshroom/mooshroom_mycelium.png"), false, false, Optional.empty(), 0);

    public Optional<MushroomType> getMushroomType(LevelAccessor level) {
        if (this.customMushroom.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(BovineRegistryUtil.getMushroomTypeFromKey(level, this.customMushroom.get()));
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof MushroomCowConfiguration other))
            return false;

        return other.blockState.equals(this.blockState) && other.blockModelLocation.equals(this.blockModelLocation) && other.blockModelVariant.equals(this.blockModelVariant) && other.customMushroom.equals(this.customMushroom) && other.cowTexture.equals(this.cowTexture) && other.backTexture.equals(this.backTexture) && other.backGrassTinted == this.backGrassTinted && other.canMakeSuspiciousStew == this.canMakeSuspiciousStew && other.biomeTagKey.equals(this.biomeTagKey) && other.naturalSpawnWeight == this.naturalSpawnWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.blockState, this.blockModelLocation, this.blockModelVariant, this.cowTexture, this.backTexture, this.backGrassTinted, this.canMakeSuspiciousStew, this.biomeTagKey, this.naturalSpawnWeight);
    }
}
