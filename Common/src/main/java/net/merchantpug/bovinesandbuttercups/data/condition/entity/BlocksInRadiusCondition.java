package net.merchantpug.bovinesandbuttercups.data.condition.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.api.condition.ConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.block.BlockConfiguredCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;
import java.util.stream.Collectors;

public class BlocksInRadiusCondition extends ConditionConfiguration<Entity> {
    public static final MapCodec<BlocksInRadiusCondition> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.list(BlockConfiguredCondition.CODEC).fieldOf("block_conditions").forGetter(BlocksInRadiusCondition::getConditions),
            Codec.DOUBLE.fieldOf("radius").forGetter(BlocksInRadiusCondition::getRadius),
            Vec3.CODEC.optionalFieldOf("offset").forGetter(BlocksInRadiusCondition::getOffset)
    ).apply(builder, BlocksInRadiusCondition::new));

    private final List<ConfiguredCondition<BlockInWorld, ?, ?>> blockConditions;
    private final double radius;
    private final Optional<Vec3> offset;

    public BlocksInRadiusCondition(List<ConfiguredCondition<BlockInWorld, ?, ?>> blockConditions, double radius, Optional<Vec3> offset) {
        this.blockConditions = blockConditions;
        this.radius = radius;
        this.offset = offset;
    }

    @Override
    public boolean test(Entity entity) {
        AABB box = new AABB(entity.blockPosition()).inflate(radius);
        if (offset.isPresent()) {
            box = box.move(offset.get());
        }

        HashMap<ConfiguredCondition<BlockInWorld, ?, ?>, Boolean> conditionStates = new HashMap<>();
        blockConditions.forEach(condition -> conditionStates.put(condition, false));

        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            Set<? extends ConfiguredCondition<BlockInWorld, ?, ?>> conditions = conditionStates.entrySet().stream().filter(entry -> !entry.getValue()).map(Map.Entry::getKey).collect(Collectors.toSet());

            for (ConfiguredCondition<BlockInWorld, ?, ?> condition : conditions) {
                conditionStates.put(condition, condition.test(new BlockInWorld(entity.level, pos, false)));
            }
        }

        return conditionStates.values().stream().allMatch(Boolean::booleanValue);
    }

    @Override
    public void returnCowFeedback(LivingEntity parent, ParticleOptions particle) {
        AABB box = new AABB(parent.blockPosition()).inflate(radius);
        if (offset.isPresent()) {
            box = box.move(offset.get());
        }

        HashMap<ConfiguredCondition<BlockInWorld, ?, ?>, BlockPos> posMap = new HashMap<>();

        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            BlockState state = parent.level.getBlockState(pos);
            if (state.getShape(parent.level, pos).isEmpty()) continue;

            for (ConfiguredCondition<BlockInWorld, ?, ?> condition : blockConditions) {
                if (condition.test(new BlockInWorld(parent.level, pos, false)) && (!posMap.containsKey(condition) || pos.distSqr(parent.blockPosition()) < posMap.get(condition).distSqr(parent.blockPosition())))
                    posMap.put(condition, pos.immutable());
            }
        }

        posMap.values().forEach(pos -> {
            VoxelShape shape = parent.level.getBlockState(pos).getShape(parent.level, pos);
            AABB blockBox = shape.bounds();
            createParticleTrail(parent, blockBox.getCenter().add(new Vec3(pos.getX(), pos.getY(), pos.getZ())), particle);
        });
    }

    private void createParticleTrail(Entity parent, Vec3 pos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(parent.position()) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value) {
            ((ServerLevel)parent.level).sendParticles(options, Mth.lerp(d, pos.x(), parent.position().x()), Mth.lerp(d, pos.y(), parent.position().y()), Mth.lerp(d, pos.z(), parent.position().z()), 1, 0.05, 0.05, 0.05, 0.01);
        }
    }

    public List<ConfiguredCondition<BlockInWorld, ?, ?>> getConditions() {
        return blockConditions;
    }

    public double getRadius() {
        return radius;
    }

    public Optional<Vec3> getOffset() {
        return offset;
    }
}
