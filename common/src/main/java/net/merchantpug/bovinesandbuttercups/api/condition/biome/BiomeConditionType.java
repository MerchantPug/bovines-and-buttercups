package net.merchantpug.bovinesandbuttercups.api.condition.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

public class BiomeConditionType<CC extends ConditionConfiguration<Holder<Biome>>> extends ConditionType<Holder<Biome>, CC> {
    public static final Codec<BiomeConditionType<?>> CODEC = Services.PLATFORM.getBiomeConditionTypeCodec();

    public BiomeConditionType(MapCodec<CC> codec) {
        super(codec);
    }

    public Codec<ConfiguredCondition<Holder<Biome>, CC, ?>> getCodec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        codec.forGetter(ConfiguredCondition::getConfiguration)
                ).apply(instance, (t1) -> new ConfiguredCondition<>(this, t1)));
    }
}
