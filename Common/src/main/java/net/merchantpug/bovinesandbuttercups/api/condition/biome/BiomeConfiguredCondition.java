package net.merchantpug.bovinesandbuttercups.api.condition.biome;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public class BiomeConfiguredCondition<CC extends ConditionConfiguration<Holder<Biome>>, CT extends ConditionType<Holder<Biome>, CC>> extends ConfiguredCondition<Holder<Biome>, CC, CT> {
    public static final Codec<ConfiguredCondition<Holder<Biome>, ?, ?>> CODEC = BiomeConditionType.CODEC.dispatch(BiomeConfiguredCondition::getType, BiomeConditionType::getCodec);

    public BiomeConfiguredCondition(CT conditionType, CC conditionConfiguration) {
        super(conditionType, conditionConfiguration);
    }
}
