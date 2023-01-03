package net.merchantpug.bovinesandbuttercups.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class BreedingConditionConfiguration {
    private final double radius;
    private final List<BlockPredicate> predicates;
    private final Optional<ParticleOptions> particleOptions;

    public static final Codec<BreedingConditionConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.DOUBLE.optionalFieldOf("radius", 8.0).forGetter(BreedingConditionConfiguration::getRadius),
            Codec.list(BlockPredicate.CODEC).fieldOf("predicates").forGetter(BreedingConditionConfiguration::getBlockPredicates),
            ParticleTypes.CODEC.optionalFieldOf("particle").orElseGet(Optional::empty).forGetter(BreedingConditionConfiguration::getParticleOptions)
    ).apply(builder, BreedingConditionConfiguration::new));

    public BreedingConditionConfiguration(double radius, List<BlockPredicate> predicates, Optional<ParticleOptions> particleOptions) {
        this.radius = radius;
        this.predicates = predicates;
        this.particleOptions = particleOptions;
    }

    public double getRadius() {
        return radius;
    }

    public List<BlockPredicate> getBlockPredicates() {
        return predicates;
    }

    public Optional<ParticleOptions> getParticleOptions() {
        return particleOptions;
    }

    public void spawnParticleTrail(Entity entity, LevelAccessor level) {
        if (particleOptions.isEmpty()) return;
        Map<BlockState, BlockPos> stateMap = new HashMap<>();
        BlockPos.betweenClosedStream(new AABB(entity.blockPosition()).inflate(radius - 1)).forEach(pos -> {
            BlockState state = level.getBlockState(pos);

            predicates.forEach(blockPredicate -> {
                if (blockPredicate.operation != PredicateOperation.NOT && (blockPredicate.blocks.isPresent() && blockPredicate.blocks.get().contains(state.getBlock()) || blockPredicate.states.isPresent() && blockPredicate.states.get().contains(state)) && (!stateMap.containsKey(state) || pos.distSqr(entity.blockPosition()) < stateMap.get(state).distSqr(entity.blockPosition()))) {
                    stateMap.put(state, pos.immutable());
                }
            });
        });
        stateMap.forEach((blockState, pos) -> {
            Vec3 centeredBlockPos = Vec3.atCenterOf(pos);

            double value = (1 - (1 / (centeredBlockPos.distanceTo(entity.position()) + 1))) / 4;

            for (double d = 0.0; d < 1.0; d += value) {
                ((ServerLevel)entity.level).sendParticles(particleOptions.get(), Mth.lerp(d, entity.position().x(), centeredBlockPos.x()), Mth.lerp(d, entity.position().y(), centeredBlockPos.y()), Mth.lerp(d, entity.position().z(), centeredBlockPos.z()), 1, 0, 0, 0, 0);
            }
        });
    }

    public boolean test(BlockPos origin, LevelAccessor level) {
        HashMap<BlockPredicate, Set<BlockState>> predicateValues = new HashMap<>();
        predicates.forEach(blockPredicate -> predicateValues.put(blockPredicate, new HashSet<>()));

        level.getBlockStates(new AABB(origin).inflate(radius - 1)).forEach(state -> {
            for (Map.Entry<BlockPredicate, Set<BlockState>> entry : predicateValues.entrySet()) {
                if (!entry.getValue().contains(state) && (entry.getKey().blocks().isPresent() && entry.getKey().blocks().get().contains(state.getBlock()) || entry.getKey().states().isPresent() && entry.getKey().states().get().contains(state))) {
                    entry.getValue().add(state);
                    predicateValues.put(entry.getKey(), entry.getValue());
                }
            }
        });

        return predicateValues.entrySet().stream().allMatch(entry -> {
            if (entry.getKey().operation == PredicateOperation.AND && (entry.getKey().blocks.isEmpty() || new HashSet<>(entry.getValue().stream().map(BlockBehaviour.BlockStateBase::getBlock).toList()).containsAll(entry.getKey().blocks.get())) && (entry.getKey().states.isEmpty() || entry.getValue().containsAll(entry.getKey().states.get())))
                return true;
            else if (entry.getKey().operation == PredicateOperation.OR && entry.getValue().stream().anyMatch(state -> entry.getKey().blocks.isPresent() && entry.getKey().blocks.get().contains(state.getBlock())) || entry.getValue().stream().anyMatch(state -> entry.getKey().states.isPresent() && entry.getKey().states.get().contains(state)))
                return true;
            else
                return entry.getKey().operation == PredicateOperation.NOT && entry.getValue().stream().noneMatch(state -> entry.getKey().blocks.isPresent() && entry.getKey().blocks.get().contains(state.getBlock())) && entry.getValue().stream().noneMatch(state -> entry.getKey().states.isPresent() && entry.getKey().states.get().contains(state));
        });
    }

    public record BlockPredicate(Optional<List<Block>> blocks, Optional<List<BlockState>> states, PredicateOperation operation) {
        public static final Codec<BlockPredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.list(Registry.BLOCK.byNameCodec()).optionalFieldOf("blocks").orElseGet(Optional::empty).forGetter(BlockPredicate::blocks),
                Codec.list(BlockState.CODEC).optionalFieldOf("block_states").orElseGet(Optional::empty).forGetter(BlockPredicate::states),
                PredicateOperation.CODEC.optionalFieldOf("operation", PredicateOperation.AND).forGetter(BlockPredicate::operation)
        ).apply(builder, BlockPredicate::new));
    }

    public enum PredicateOperation {
        AND,
        OR,
        NOT;

        public static final PrimitiveCodec<PredicateOperation> CODEC = new PrimitiveCodec<>() {
            @Override
            public <T> DataResult<PredicateOperation> read(final DynamicOps<T> ops, final T input) {
                return ops.getStringValue(input).map(s -> fromName(s).orElseGet(() -> {
                    BovinesAndButtercups.LOG.warn("Could not get predicate operation from string '{}'. Must be 'and', 'or' or 'not'. Defaulting to 'and'.", s);
                    return PredicateOperation.AND;
                }));
            }

            @Override
            public <T> T write(final DynamicOps<T> ops, final PredicateOperation value) {
                return ops.createString(value.name());
            }

            @Override
            public String toString() {
                return "BovinesAndButtercupsPredicateOperation";
            }
        };

        public static Optional<PredicateOperation> fromName(String name) {
            for (PredicateOperation type : PredicateOperation.values()) {
                if (type.name().equals(name.toUpperCase(Locale.ROOT))) {
                    return Optional.of(type);
                }
            }
            return Optional.empty();
        }
    }
}

