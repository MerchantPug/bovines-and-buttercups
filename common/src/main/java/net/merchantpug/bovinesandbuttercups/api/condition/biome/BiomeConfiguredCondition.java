package net.merchantpug.bovinesandbuttercups.api.condition.biome;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;

public class BiomeConfiguredCondition<CC extends ConditionConfiguration<Holder<Biome>>, CT extends ConditionType<Holder<Biome>, CC>> extends ConfiguredCondition<Holder<Biome>, CC, CT> {
    public BiomeConfiguredCondition(CT conditionType, CC conditionConfiguration) {
        super(conditionType, conditionConfiguration);
    }

    public static Codec<ConfiguredCondition<Holder<Biome>, ?, ?>> getCodec() {
        return BiomeConditionType.CODEC.dispatch(BiomeConfiguredCondition::getType, BiomeConditionType::getCodec);
    }
}
