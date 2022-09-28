package net.merchantpug.bovinesandbuttercups.data.entity;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowTypeRegistryUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Optional;

public record FlowerCowBreedingRequirements(List<Pair<ResourceLocation, ResourceLocation>> parentCombinations,
                                            Optional<TagKey<Item>> associatedItems,
                                            Optional<TagKey<Biome>> associatedBiomes,
                                            Float chance,
                                            Float boostedChance) {

    public static final MapCodec<FlowerCowBreedingRequirements> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.list(Codec.pair(ResourceLocation.CODEC, ResourceLocation.CODEC)).fieldOf("parent_combinations").forGetter(FlowerCowBreedingRequirements::parentCombinations),
            TagKey.codec(Registry.ITEM_REGISTRY).optionalFieldOf("associated_item_tag").orElseGet(Optional::empty).forGetter(FlowerCowBreedingRequirements::associatedItems),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("associated_biome_tag").orElseGet(Optional::empty).forGetter(FlowerCowBreedingRequirements::associatedBiomes),
            Codec.FLOAT.fieldOf("chance").forGetter(FlowerCowBreedingRequirements::chance),
            Codec.FLOAT.optionalFieldOf("boosted_chance").orElseGet(Optional::empty).forGetter(x -> Optional.of(x.boostedChance))
    ).apply(builder, (t1, t2, t3, t4, t5) -> new FlowerCowBreedingRequirements(t1, t2, t3, t4, t5.orElse(t4))));

    public boolean doesApply(LevelAccessor level, ConfiguredCowType<FlowerCowConfiguration, ?> parent, ConfiguredCowType<FlowerCowConfiguration, ?> otherParent) {
        return parentCombinations.stream().anyMatch(pair -> pair.getFirst().equals(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(level, parent)) && pair.getSecond().equals(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(level, otherParent)) || pair.getFirst().equals(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(level, otherParent)) && pair.getSecond().equals(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(level, parent)));
    }

    public boolean isBoosted(FlowerCowConfiguration parent, FlowerCowConfiguration otherParent) {
        return false;
    }
}
