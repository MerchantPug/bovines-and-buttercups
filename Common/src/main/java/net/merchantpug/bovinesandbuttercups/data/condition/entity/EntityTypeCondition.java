package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.data.condition.block.BlockLocationCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.stream.Collectors;

public class EntityTypeCondition extends ConditionConfiguration<Entity> {
    public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.either(TagKey.hashedCodec(Registry.ENTITY_TYPE_REGISTRY), Registry.ENTITY_TYPE.holderByNameCodec()).fieldOf("location").xmap(tagKeyHolderEither ->
                    tagKeyHolderEither.map(Registry.ENTITY_TYPE::getOrCreateTag, entityType ->(HolderSet<EntityType<?>>)HolderSet.direct(entityType)),
                    holders -> holders.unwrap().mapBoth(tagKey -> tagKey, holders1 -> holders1.get(0))).forGetter(EntityTypeCondition::getEntityTypes)
    ).apply(builder, EntityTypeCondition::new));

    private final HolderSet<EntityType<?>> entityTypes;

    public EntityTypeCondition(HolderSet<EntityType<?>> entityTypes) {
        this.entityTypes = entityTypes;
    }

    @Override
    public boolean test(Entity entity) {
        return entityTypes.contains(entity.getType().builtInRegistryHolder());
    }

    public HolderSet<EntityType<?>> getEntityTypes() {
        return entityTypes;
    }
}
