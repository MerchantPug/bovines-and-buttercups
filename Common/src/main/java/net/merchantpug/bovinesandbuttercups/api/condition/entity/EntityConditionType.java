package net.merchantpug.bovinesandbuttercups.api.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class EntityConditionType<CC extends ConditionConfiguration<Entity>> extends ConditionType<Entity, CC> {
    public static final Codec<EntityConditionType<?>> CODEC = Services.PLATFORM.getEntityConditionTypeCodec();

    public EntityConditionType(MapCodec<CC> codec) {
        this.configuredCodec = RecordCodecBuilder.create(instance ->
                instance.group(
                        codec.forGetter(ConfiguredCondition::getConfiguration)
                ).apply(instance, t1 -> new ConfiguredCondition<>(this, t1)));
    }

    @Override
    public Codec<ConfiguredCondition<Entity, CC, ConditionType<Entity, CC>>> getCodec() {
        return configuredCodec;
    }
}
