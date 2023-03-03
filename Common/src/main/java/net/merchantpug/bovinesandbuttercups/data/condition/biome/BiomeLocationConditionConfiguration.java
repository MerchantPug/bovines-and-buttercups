package net.merchantpug.bovinesandbuttercups.data.condition.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeLocationConditionConfiguration extends ConditionConfiguration<Holder<Biome>> {
    public static final MapCodec<BiomeLocationConditionConfiguration> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.either(TagKey.hashedCodec(Registry.BIOME_REGISTRY), BuiltinRegistries.BIOME.holderByNameCodec()).fieldOf("location").xmap(tagKeyBiomeEither ->
                            tagKeyBiomeEither.map(BuiltinRegistries.BIOME::getOrCreateTag, biome ->(HolderSet<Biome>)HolderSet.direct(biome)),
                    holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(BiomeLocationConditionConfiguration::getBiomes)
    ).apply(builder, BiomeLocationConditionConfiguration::new));

    private final HolderSet<Biome> biomes;

    public BiomeLocationConditionConfiguration(HolderSet<Biome> biomes) {
        this.biomes = biomes;
    }

    @Override
    public boolean test(Holder<Biome> biome) {
        return biomes.contains(biome);
    }

    public HolderSet<Biome> getBiomes() {
        return biomes;
    }
}
