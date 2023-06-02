package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.biome.BiomeConfiguredCondition;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;

public class BiomeConditionCondition extends ConditionConfiguration<Entity> {
    public static MapCodec<BiomeConditionCondition> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                BiomeConfiguredCondition.getCodec(registryAccess).fieldOf("biome_condition").forGetter(BiomeConditionCondition::getBiomeCondition)
        ).apply(builder, BiomeConditionCondition::new));
    }

    private final ConfiguredCondition<Holder<Biome>, ?, ?> biomeCondition;

    public BiomeConditionCondition(ConfiguredCondition<Holder<Biome>, ?, ?> biomeCondition) {
        this.biomeCondition = biomeCondition;
    }

    @Override
    public boolean test(Entity entity) {
        return biomeCondition.test(entity.level().getBiome(entity.blockPosition()));
    }

    @Override
    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {
        ((ServerLevel)parent.level()).sendParticles(particle, parent.position().x(), parent.position().y(), parent.position().z(), 12, 1.0, 1.0, 1.0, 0.01);
    }

    public ConfiguredCondition<Holder<Biome>, ?, ?> getBiomeCondition() {
        return biomeCondition;
    }
}
