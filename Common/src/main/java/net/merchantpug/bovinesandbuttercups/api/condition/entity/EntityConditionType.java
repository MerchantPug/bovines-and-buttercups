package net.merchantpug.bovinesandbuttercups.api.condition.entity;

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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.biome.Biome;

import java.util.function.Function;

public class EntityConditionType<CC extends ConditionConfiguration<Entity>> extends ConditionType<Entity, CC> {
    public static final Codec<EntityConditionType<?>> CODEC = Services.PLATFORM.getEntityConditionTypeCodec();

    public EntityConditionType(Function<RegistryAccess, MapCodec<CC>> codecFunction) {
        super(codecFunction);
    }

    public Codec<ConfiguredCondition<Entity, CC, ?>> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        codecFunction.apply(registryAccess).forGetter(ConfiguredCondition::getConfiguration)
                ).apply(instance, (t1) -> new ConfiguredCondition(this, t1)));
    }
}
