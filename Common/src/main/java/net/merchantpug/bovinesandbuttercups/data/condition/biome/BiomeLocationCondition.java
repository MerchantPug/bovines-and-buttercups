package net.merchantpug.bovinesandbuttercups.data.condition.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class BiomeLocationCondition extends ConditionConfiguration<Holder<Biome>> {
    public static MapCodec<BiomeLocationCondition> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.either(TagKey.hashedCodec(Registries.BIOME), registryAccess.registryOrThrow(Registries.BIOME).holderByNameCodec()).fieldOf("location").xmap(tagKeyBiomeEither ->
                        tagKeyBiomeEither.map(registryAccess.registryOrThrow(Registries.BIOME)::getOrCreateTag, biome ->(HolderSet<Biome>)HolderSet.direct(biome)),
                holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(BiomeLocationCondition::getBiomes)
        ).apply(builder, BiomeLocationCondition::new));
    }

    private final HolderSet<Biome> biomes;

    public BiomeLocationCondition(HolderSet<Biome> biomes) {
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
