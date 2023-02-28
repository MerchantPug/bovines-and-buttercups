package net.merchantpug.bovinesandbuttercups.data.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.condition.ConfiguredCondition;
import net.merchantpug.bovinesandbuttercups.api.condition.entity.EntityConfiguredCondition;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class BreedingConditionConfiguration {
    private final double radius;
    private final Optional<ConfiguredCondition<Entity, ?, ?>> condition;
    private final Optional<ConfiguredCondition<Entity, ?, ?>> otherCondition;
    private final List<BlockPredicate> predicates;
    private final boolean includesAssociatedBlocks;

    public static final Codec<BreedingConditionConfiguration> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.DOUBLE.optionalFieldOf("radius", 6.0).forGetter(BreedingConditionConfiguration::getRadius),
            EntityConfiguredCondition.CODEC.optionalFieldOf("condition").forGetter(BreedingConditionConfiguration::getCondition),
            EntityConfiguredCondition.CODEC.optionalFieldOf("other_condition").forGetter(BreedingConditionConfiguration::getOtherCondition),
            Codec.list(BlockPredicate.CODEC).optionalFieldOf("predicates", List.of()).forGetter(BreedingConditionConfiguration::getBlockPredicates),
            Codec.BOOL.optionalFieldOf("includes_associated_blocks", true).forGetter(BreedingConditionConfiguration::shouldIncludeAssociatedBlocks)
    ).apply(builder, BreedingConditionConfiguration::new));

    public BreedingConditionConfiguration(double radius, Optional<ConfiguredCondition<Entity, ?, ?>> condition, Optional<ConfiguredCondition<Entity, ?, ?>> otherCondition, List<BlockPredicate> predicates, boolean includesAssociatedBlocks) {
        this.radius = radius;
        this.condition = condition;
        this.otherCondition = otherCondition;
        this.predicates = predicates;
        this.includesAssociatedBlocks = includesAssociatedBlocks;
    }

    public double getRadius() {
        return radius;
    }

    public Optional<ConfiguredCondition<Entity, ?, ?>> getCondition() {
        return condition;
    }

    public Optional<ConfiguredCondition<Entity, ?, ?>> getOtherCondition() {
        return otherCondition;
    }

    public List<BlockPredicate> getBlockPredicates() {
        return predicates;
    }

    public boolean shouldIncludeAssociatedBlocks() {
        return includesAssociatedBlocks;
    }

    @Deprecated
    public record BlockPredicate(Optional<List<Block>> blocks, Optional<List<BlockState>> states, PredicateOperation operation) {
        public static final Codec<BlockPredicate> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.list(Registry.BLOCK.byNameCodec()).optionalFieldOf("blocks").forGetter(BlockPredicate::blocks),
                Codec.list(BlockState.CODEC).optionalFieldOf("block_states").forGetter(BlockPredicate::states),
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

