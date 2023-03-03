package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.data.meta.NotConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class EntitiesInRadiusCondition extends ConditionConfiguration<Entity> {
    public static MapCodec<EntitiesInRadiusCondition> getCodec(RegistryAccess registryAccess) {
        return RecordCodecBuilder.mapCodec(builder -> builder.group(
                Codec.list(EntityConfiguredCondition.getCodec(registryAccess)).fieldOf("entity_conditions").forGetter(EntitiesInRadiusCondition::getConditions),
                Codec.DOUBLE.fieldOf("radius").forGetter(EntitiesInRadiusCondition::getRadius),
                Vec3.CODEC.optionalFieldOf("offset").forGetter(EntitiesInRadiusCondition::getOffset)
        ).apply(builder, EntitiesInRadiusCondition::new));
    }

    private final List<ConfiguredCondition<Entity, ?, ?>> entityConditions;
    private final double radius;
    private final Optional<Vec3> offset;

    public EntitiesInRadiusCondition(List<ConfiguredCondition<Entity, ?, ?>> blockConditions, double radius, Optional<Vec3> offset) {
        this.entityConditions = blockConditions;
        this.radius = radius;
        this.offset = offset;
    }

    @Override
    public boolean test(Entity entity) {
        AABB box = new AABB(entity.blockPosition()).inflate(radius);
        if (offset.isPresent()) {
            box = box.move(offset.get());
        }

        HashMap<ConfiguredCondition<Entity, ?, ?>, Boolean> conditionStates = new HashMap<>();
        entityConditions.forEach(condition -> conditionStates.put(condition, false));

        for (ConfiguredCondition<Entity, ?, ?> condition : entityConditions.stream().filter(condition -> !(condition.getConfiguration() instanceof NotConditionConfiguration<Entity>)).collect(Collectors.toSet())) {
            if (entity.level.getEntities(entity, box, condition).stream().findFirst().isPresent())
                conditionStates.put(condition, true);
        }

        return conditionStates.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {
        AABB box = new AABB(parent.blockPosition()).inflate(radius);
        if (offset.isPresent()) {
            box = box.move(offset.get());
        }
        AABB finalBox = box;

        HashMap<ConfiguredCondition<Entity, ?, ?>, Entity> entityMap = new HashMap<>();

        for (ConfiguredCondition<Entity, ?, ?> condition : entityConditions) {
            parent.level.getEntities(parent, finalBox, condition).forEach(entity -> {
                if (!entityMap.containsKey(condition) || parent.distanceToSqr(entity) < parent.distanceToSqr(entityMap.get(condition)))
                    entityMap.put(condition, entity);
            });
        }

        entityMap.values().forEach(entity -> createParticleTrail(parent, new Vec3(entity.getX(), entity.getY(), entity.getZ()), particle));
    }

    private void createParticleTrail(Entity parent, Vec3 pos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(parent.position()) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value) {
            ((ServerLevel)parent.level).sendParticles(options, Mth.lerp(d, pos.x(), parent.position().x()), Mth.lerp(d, pos.y(), parent.position().y()), Mth.lerp(d, pos.z(), parent.position().z()), 1, 0.05, 0.05, 0.05, 0.01);
        }
    }

    public List<ConfiguredCondition<Entity, ?, ?>> getConditions() {
        return entityConditions;
    }

    public double getRadius() {
        return radius;
    }

    public Optional<Vec3> getOffset() {
        return offset;
    }
}
