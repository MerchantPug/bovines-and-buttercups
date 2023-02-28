package net.merchantpug.bovinesandbuttercups.util;

import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomHugeMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.particle.ShroomParticleOptions;
import net.merchantpug.bovinesandbuttercups.data.entity.BreedingConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import net.merchantpug.bovinesandbuttercups.registry.BovineCriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MushroomCowChildTypeUtil {
    public static ResourceLocation chooseMooshroomBabyType(MushroomCow parent, MushroomCow other, MushroomCow child, @Nullable Player player) {
        List<ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>> eligibleCowTypes = new ArrayList<>();
        boolean bl = false;
        Level level = parent.level;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream().filter(type -> type.getConfiguration() instanceof MushroomCowConfiguration).toList()) {
            ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> mushroomCowType = (ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>>) cowType;
            if (mushroomCowType.getConfiguration().getBreedingConditions().isEmpty()) continue;
            var conditions = mushroomCowType.getConfiguration().getBreedingConditions().get();

            if (conditions.getCondition().isPresent() && conditions.getOtherCondition().isPresent() && conditions.getCondition().get().test(parent) && conditions.getOtherCondition().get().test(other))
                eligibleCowTypes.add(mushroomCowType);
            else if (conditions.getOtherCondition().isEmpty() && conditions.getCondition().isPresent() && conditions.getCondition().get().test(parent))
                eligibleCowTypes.add(mushroomCowType);
            else if (conditions.getCondition().isEmpty() && conditions.getOtherCondition().isPresent() && conditions.getOtherCondition().get().test(other)) {
                bl = true;
                eligibleCowTypes.add(mushroomCowType);
            } else if (conditions.getCondition().isEmpty() && conditions.getOtherCondition().isEmpty() && testBreedingBlocks(parent, mushroomCowType.getConfiguration(), level)) {
                eligibleCowTypes.add(mushroomCowType);
            }
        }


        ResourceLocation parentTypeKey = BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(parent));
        ResourceLocation otherTypeKey = BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(other));

        if (!eligibleCowTypes.isEmpty()) {
            int random = parent.getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);
            if (!bl && randomType.getConfiguration().getBreedingConditions().isPresent() && randomType.getConfiguration().getBreedingConditions().get().getCondition().isPresent() && randomType.getConfiguration().getColor().isPresent())
                randomType.getConfiguration().getBreedingConditions().get().getCondition().get().returnCowFeedback(parent, new ShroomParticleOptions(randomType.getConfiguration().getColor().get()));
            else if (bl && randomType.getConfiguration().getBreedingConditions().isPresent() && randomType.getConfiguration().getBreedingConditions().get().getOtherCondition().isPresent() && randomType.getConfiguration().getColor().isPresent())
                randomType.getConfiguration().getBreedingConditions().get().getCondition().get().returnCowFeedback(other, new ShroomParticleOptions(randomType.getConfiguration().getColor().get()));
            else
                spawnParticleToBreedPosition(parent, randomType.getConfiguration(), level);

            ResourceLocation randomTypeKey = BovineRegistryUtil.getConfiguredCowTypeKey(randomType);

            if (player instanceof ServerPlayer serverPlayer && !randomTypeKey.equals(parentTypeKey) && !randomTypeKey.equals(otherTypeKey)) {
                BovineCriteriaTriggers.MUTATION.trigger(serverPlayer, parent, other, child, randomTypeKey);
            }
            return randomTypeKey;
        }

        if (!otherTypeKey.equals(parentTypeKey) && parent.getRandom().nextBoolean())
            return otherTypeKey;

        return parentTypeKey;
    }

    public static boolean testBreedingBlocks(MushroomCow parent, MushroomCowConfiguration configuration, LevelAccessor level) {
        Optional<BreedingConditionConfiguration> breedingCondition = configuration.getBreedingConditions();

        if (breedingCondition.isEmpty() || breedingCondition.get().getBlockPredicates().isEmpty() && !breedingCondition.get().shouldIncludeAssociatedBlocks())
            return false;

        HashMap<BreedingConditionConfiguration.BlockPredicate, Set<BlockState>> predicateValues = new HashMap<>();
        breedingCondition.get().getBlockPredicates().forEach(blockPredicate -> predicateValues.put(blockPredicate, new HashSet<>()));

        double radius = breedingCondition.get().getRadius();
        boolean associatedBlockFound = false;

        AABB box = new AABB(parent.blockPosition()).move(0, radius - 2, 0).inflate(radius - 1);
        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            BlockState state = level.getBlockState(pos);

            for (Map.Entry<BreedingConditionConfiguration.BlockPredicate, Set<BlockState>> entry : predicateValues.entrySet()) {
                if (!entry.getValue().contains(state) && (entry.getKey().blocks().isPresent() && entry.getKey().blocks().get().contains(state.getBlock()) || entry.getKey().states().isPresent() && entry.getKey().states().get().contains(state))) {
                    entry.getValue().add(state);
                    predicateValues.put(entry.getKey(), entry.getValue());
                }
            }
            if (breedingCondition.get().shouldIncludeAssociatedBlocks()) {
                if (configuration.getMushroom().blockState().isPresent() && (state.is(configuration.getMushroom().blockState().get().getBlock()) || state.getBlock() instanceof FlowerPotBlock && ((FlowerPotBlock)state.getBlock()).getContent() == configuration.getMushroom().blockState().get().getBlock())) {
                    associatedBlockFound = true;
                    break;
                } else if (configuration.getMushroom().getMushroomType().isPresent() &&
                        (state.is(BovineBlocks.CUSTOM_MUSHROOM.get()) && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity && mushroomBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get() || state.is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlockEntity && hugeMushroomBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get() || state.is(BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()) && level.getBlockEntity(pos) instanceof CustomMushroomPotBlockEntity mushroomPotBlockEntity && mushroomPotBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get())) {
                    associatedBlockFound = true;
                    break;
                }
            }
        }

        if (associatedBlockFound)
            return true;

        if (predicateValues.isEmpty())
            return false;

        return predicateValues.entrySet().stream().allMatch(entry -> {
            if (entry.getKey().operation() == BreedingConditionConfiguration.PredicateOperation.AND && (entry.getKey().blocks().isEmpty() || new HashSet<>(entry.getValue().stream().map(BlockBehaviour.BlockStateBase::getBlock).toList()).containsAll(entry.getKey().blocks().get())) && (entry.getKey().states().isEmpty() || entry.getValue().containsAll(entry.getKey().states().get())))
                return true;
            else if (entry.getKey().operation() == BreedingConditionConfiguration.PredicateOperation.OR && entry.getValue().stream().anyMatch(state -> entry.getKey().blocks().isPresent() && entry.getKey().blocks().get().contains(state.getBlock())) || entry.getValue().stream().anyMatch(state -> entry.getKey().states().isPresent() && entry.getKey().states().get().contains(state)))
                return true;
            else
                return entry.getKey().operation() == BreedingConditionConfiguration.PredicateOperation.NOT && entry.getValue().stream().noneMatch(state -> entry.getKey().blocks().isPresent() && entry.getKey().blocks().get().contains(state.getBlock())) && entry.getValue().stream().noneMatch(state -> entry.getKey().states().isPresent() && entry.getKey().states().get().contains(state));
        });
    }

    public static void spawnParticleToBreedPosition(MushroomCow parent, MushroomCowConfiguration configuration, LevelAccessor level) {
        Optional<BreedingConditionConfiguration> breedingCondition = configuration.getBreedingConditions();
        if (breedingCondition.isEmpty() || configuration.getColor().isEmpty()) return;
        double radius = breedingCondition.get().getRadius();
        Map<BlockState, BlockPos> stateMap = new HashMap<>();

        AABB box = new AABB(parent.blockPosition()).move(0, radius - 1, 0).inflate(radius);
        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            BlockState state = level.getBlockState(pos);

            breedingCondition.get().getBlockPredicates().forEach(blockPredicate -> {
                if (blockPredicate.operation() != BreedingConditionConfiguration.PredicateOperation.NOT && (blockPredicate.blocks().isPresent() && blockPredicate.blocks().get().contains(state.getBlock()) || blockPredicate.states().isPresent() && blockPredicate.states().get().contains(state)) && (!stateMap.containsKey(state) || pos.distSqr(parent.blockPosition()) < stateMap.get(state).distSqr(parent.blockPosition()))) {
                    stateMap.put(state, pos.immutable());
                }
            });

            if (breedingCondition.get().shouldIncludeAssociatedBlocks()) {
                if (configuration.getMushroom().blockState().isPresent() && (state.is(configuration.getMushroom().blockState().get().getBlock()) || state.getBlock() instanceof FlowerPotBlock && ((FlowerPotBlock)state.getBlock()).getContent() == configuration.getMushroom().blockState().get().getBlock())) {
                    stateMap.clear();
                    stateMap.put(state, pos.immutable());
                    break;
                } else if (configuration.getMushroom().getMushroomType().isPresent() &&
                        (state.is(BovineBlocks.CUSTOM_MUSHROOM.get()) && level.getBlockEntity(pos) instanceof CustomMushroomBlockEntity mushroomBlockEntity && mushroomBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get() || state.is(BovineBlocks.CUSTOM_MUSHROOM_BLOCK.get()) && level.getBlockEntity(pos) instanceof CustomHugeMushroomBlockEntity hugeMushroomBlockEntity && hugeMushroomBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get() || state.is(BovineBlocks.POTTED_CUSTOM_MUSHROOM.get()) && level.getBlockEntity(pos) instanceof CustomMushroomPotBlockEntity mushroomPotBlockEntity && mushroomPotBlockEntity.getMushroomType() == configuration.getMushroom().getMushroomType().get())) {
                    stateMap.clear();
                    stateMap.put(state, pos.immutable());
                    break;
                }
            }
        }

        stateMap.forEach((state, pos) -> {
            VoxelShape shape = state.getShape(level, pos);
            if (shape.isEmpty()) return;
            AABB blockBox = shape.bounds();
            createParticleTrail(parent, blockBox.getCenter().add(new Vec3(pos.getX(), pos.getY(), pos.getZ())), new ShroomParticleOptions(configuration.getColor().get()));
        });
    }

    public static void createParticleTrail(MushroomCow parent, Vec3 pos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(parent.position()) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value) {
            ((ServerLevel)parent.level).sendParticles(options, Mth.lerp(d, pos.x(), parent.position().x()), Mth.lerp(d, pos.y(), parent.position().y()), Mth.lerp(d, pos.z(), parent.position().z()), 1, 0, 0, 0, 0);
        }
    }
}
