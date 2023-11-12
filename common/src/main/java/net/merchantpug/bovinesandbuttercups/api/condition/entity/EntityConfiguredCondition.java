package net.merchantpug.bovinesandbuttercups.api.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionType;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.Entity;

public class EntityConfiguredCondition<CC extends ConditionConfiguration<Entity>, CT extends ConditionType<Entity, CC>> extends ConfiguredCondition<Entity, CC, CT> {
    public EntityConfiguredCondition(CT conditionType, CC conditionConfiguration) {
        super(conditionType, conditionConfiguration);
    }

    public static Codec<ConfiguredCondition<Entity, ?, ?>> getCodec() {
        return EntityConditionType.CODEC.dispatch(EntityConfiguredCondition::getType, EntityConditionType::getCodec);
    }
}
