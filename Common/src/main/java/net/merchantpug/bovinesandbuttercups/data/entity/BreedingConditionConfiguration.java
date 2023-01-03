package net.merchantpug.bovinesandbuttercups.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class BreedingConditionConfiguration {
    private final double radius;
    private final List<BlockPredicate> predicates;
    private final Optional<ParticleOptions> particleOptions;
    private final boolean includesAssociatedBlock;

    public static final Codec<BreedingConditionConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.DOUBLE.optionalFieldOf("radius", 6.0).forGetter(BreedingConditionConfiguration::getRadius),
            Codec.list(BlockPredicate.CODEC).fieldOf("predicates").forGetter(BreedingConditionConfiguration::getBlockPredicates),
            ParticleTypes.CODEC.optionalFieldOf("particle").orElseGet(Optional::empty).forGetter(BreedingConditionConfiguration::getParticleOptions),
            Codec.BOOL.optionalFieldOf("includes_associated_block", true).forGetter(BreedingConditionConfiguration::shouldIncludeAssociatedBlock)
    ).apply(builder, BreedingConditionConfiguration::new));

    public BreedingConditionConfiguration(double radius, List<BlockPredicate> predicates, Optional<ParticleOptions> particleOptions, boolean includesAssociatedBlock) {
        this.radius = radius;
        this.predicates = predicates;
        this.particleOptions = particleOptions;
        this.includesAssociatedBlock = includesAssociatedBlock;
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

    public boolean shouldIncludeAssociatedBlock() {
        return includesAssociatedBlock;
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

