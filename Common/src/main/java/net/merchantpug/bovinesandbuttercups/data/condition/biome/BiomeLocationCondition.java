package net.merchantpug.bovinesandbuttercups.data.condition.biome;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.util.CodecUtil;
import net.merchantpug.bovinesandbuttercups.util.HolderUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

public class BiomeLocationCondition extends ConditionConfiguration<Holder<Biome>> {
    public static MapCodec<BiomeLocationCondition> getCodec() {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                CodecUtil.tagOrObjectCodec(Registries.BIOME, "location").forGetter(BiomeLocationCondition::getBiomes)
        ).apply(builder, BiomeLocationCondition::new));
    }

    private final HolderUtil.PsuedoHolder<Biome> biomes;

    public BiomeLocationCondition(HolderUtil.PsuedoHolder<Biome> biomes) {
        this.biomes = biomes;
    }

    @Override
    public boolean test(Holder<Biome> biome) {
        return HolderUtil.containsBiomeHolder(biome, biomes);
    }

    public HolderUtil.PsuedoHolder<Biome> getBiomes() {
        return biomes;
    }
}
