package net.merchantpug.bovinesandbuttercups.content.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerPotBlockEntity;
import net.merchantpug.bovinesandbuttercups.content.particle.BloomParticleOptions;
import net.merchantpug.bovinesandbuttercups.data.entity.BreedingConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.mixin.EntityAccessor;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.util.HolderUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlowerCow extends Cow {
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PREVIOUS_TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> POLLINATED_RESET_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TICKS_UNTIL_FLOWERS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMES_POLLINATED = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANDING_STILL_FOR_BEE_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ALLOW_SHEARING = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.BOOLEAN);
    private ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> type;
    @Nullable public Bee bee;
    private boolean hasRefreshedDimensionsForLaying;
    @Nullable private UUID lastLightningBoltUUID;

    public FlowerCow(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
        this.bee = null;
        ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> naturalSpawnType;
        if (getTotalSpawnWeight(this.level(), this.blockPosition()) > 0) {
            naturalSpawnType = getMoobloomSpawnTypeDependingOnBiome(this.level(), this.blockPosition(), this.getRandom());
        } else {
            naturalSpawnType = getMoobloomSpawnType(this.level(), this.getRandom());
        }
       this.entityData.set(TYPE_ID, BovineRegistryUtil.getConfiguredCowTypeKey(naturalSpawnType).toString());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_ID, "bovinesandbuttercups:missing");
        this.entityData.define(PREVIOUS_TYPE_ID, "");
        this.entityData.define(POLLINATED_RESET_TICKS, 0);
        this.entityData.define(TICKS_UNTIL_FLOWERS, 0);
        this.entityData.define(TIMES_POLLINATED, 0);
        this.entityData.define(STANDING_STILL_FOR_BEE_TICKS, 0);
        this.entityData.define(ALLOW_SHEARING, true);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(2, new FlowerCow.LookAtBeeGoal());
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, FlowerCow.class, 2.0F, 1.0F, 1.0F, moobloomEntity -> moobloomEntity instanceof FlowerCow && ((FlowerCow) moobloomEntity).getStandingStillForBeeTicks() > 0));
        super.registerGoals();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.getTypeId());
        if (!this.getPreviousTypeId().equals("")) {
            compound.putString("PreviousType", this.getPreviousTypeId());
        }
        compound.putInt("PollinatedResetTicks", this.getPollinatedResetTicks());
        compound.putInt("TicksUntilFlowers", this.getTicksUntilFlowers());
        compound.putInt("TimesPollinated", this.getTimesPollinated());
        compound.putBoolean("AllowShearing", this.shouldAllowShearing());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Type")) {
            this.setFlowerType(compound.getString("Type"));
        }
        if (compound.contains("PreviousType")) {
            this.setPreviousTypeId(compound.getString("PreviousType"));
        }
        if (compound.contains("PollinatedResetTicks", 99)) {
            this.setPollinatedResetTicks(compound.getInt("PollinatedResetTicks"));
        }
        if (compound.contains("TicksUntilFlowers", 99)) {
            this.setTicksUntilFlowers(compound.getInt("TicksUntilFlowers"));
        }
        if (compound.contains("TimesPollinated", 99)) {
            this.setTimesPollinated(compound.getInt("TimesPollinated"));
        }
        if (compound.contains("AllowShearing", Tag.TAG_BYTE)) {
            this.setAllowShearing(compound.getBoolean("AllowShearing"));
        }
    }

    public void setBee(@Nullable Bee value) {
        this.bee = value;
    }

    public static boolean canMoobloomSpawn(EntityType<? extends FlowerCow> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(level, pos) && getTotalSpawnWeight(level, pos) > 0;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt bolt) {
        UUID uuid = bolt.getUUID();
        if (!uuid.equals(this.lastLightningBoltUUID)) {
            if (this.getPreviousTypeId().equals("")) {
                if (this.getFlowerCowType().configuration().getSettings().thunderConverts().isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                }
                this.setPreviousTypeId(this.getTypeId());

                List<CowTypeConfiguration.WeightedConfiguredCowType> compatibleList = new ArrayList<>();
                int totalWeight = 0;

                for (CowTypeConfiguration.WeightedConfiguredCowType weightedCowType : this.getFlowerCowType().configuration().getSettings().thunderConverts().get()) {
                    if (weightedCowType.getConfiguredCowType().isEmpty()) {
                        BovinesAndButtercups.LOG.warn("Lightning struck moobloom at {} tried to get thunder conversion type '{}' that does not exist. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                        continue;
                    } else if (!(weightedCowType.getConfiguredCowType().get().configuration() instanceof FlowerCowConfiguration)) {
                        BovinesAndButtercups.LOG.warn("Lightning struck moobloom at {} tried to get thunder conversion type '{}' that is not a moobloom type. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                        continue;
                    }

                    if (weightedCowType.weight() > 0) {
                        compatibleList.add(weightedCowType);
                    }
                }

                if (compatibleList.isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                } else if (compatibleList.size() == 1) {
                    this.setFlowerType(compatibleList.get(0).configuredCowTypeResource().toString());
                } else {
                    for (CowTypeConfiguration.WeightedConfiguredCowType cct : compatibleList) {
                        totalWeight -= cct.weight();
                        if (totalWeight <= 0) {
                            this.setFlowerType(cct.configuredCowTypeResource().toString());
                            break;
                        }
                    }
                }
            } else {
                this.setFlowerType(this.getPreviousTypeId());
                this.setPreviousTypeId("");
            }
            this.lastLightningBoltUUID = uuid;
            this.playSound(BovineSoundEvents.MOOBLOOM_CONVERT.get(), 2.0F, 1.0F);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        if (bee != null && !this.level().isClientSide()) {
            this.setStandingStillForBeeTicks(0);
            bee = null;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (bee != null && !bee.isAlive() && !this.level().isClientSide()) {
            this.setStandingStillForBeeTicks(0);
            bee = null;
        }
        if (this.getStandingStillForBeeTicks() > 0 && !this.level().isClientSide())
            this.setStandingStillForBeeTicks(this.getStandingStillForBeeTicks() - 1);

        super.tick();
        if (this.getPollinatedResetTicks() > 0)
            this.setPollinatedResetTicks(this.getPollinatedResetTicks() - 1);
        else if (this.getPollinatedResetTicks() <= 0 && this.getTimesPollinated() > 0)
            this.setTimesPollinated(0);

        if (this.getTicksUntilFlowers() > 0 && !this.level().isClientSide && this.age % 8 == 0)
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.position().x(), this.position().y() + this.getBbHeight(), this.position().z(), 1, 0.3, 0.1, 0.2, 0.0);

        if (this.getStandingStillForBeeTicks() > 0) {
            if (!hasRefreshedDimensionsForLaying) {
                this.refreshDimensions();
                ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
                hasRefreshedDimensionsForLaying = true;
            }
            if (!this.level().isClientSide() && this.bee != null)
                this.getLookControl().setLookAt(bee);
        } else if (hasRefreshedDimensionsForLaying) {
            this.refreshDimensions();
            ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
            hasRefreshedDimensionsForLaying = false;
        }
    }

    public void spreadFlowers(boolean boneMealed) {
        if (this.level().isClientSide) return;

        BlockState state = null;
        if (this.getFlowerCowType().configuration().getFlower().blockState().isPresent())
            state = this.getFlowerCowType().configuration().getFlower().blockState().get().getBlock().defaultBlockState().setValue(CustomFlowerBlock.PERSISTENT, boneMealed);
        else if (this.getFlowerCowType().configuration().getFlower().getFlowerType().isPresent())
            state = BovineBlocks.CUSTOM_FLOWER.get().defaultBlockState().setValue(CustomFlowerBlock.PERSISTENT, boneMealed);

        if (state == null) {
            BovinesAndButtercups.LOG.warn("Moobloom with type '{}' tried to spread flowers without a valid flower type.", BovineRegistryUtil.getConfiguredCowTypeKey(getFlowerCowType()));
            return;
        }

        int maxTries = boneMealed ? 16 : 32;
        int xZScale = boneMealed ? 3 : 6;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for(int i = 0; i < maxTries; ++i) {
            pos.setWithOffset(this.blockPosition(), random.nextInt(xZScale) - random.nextInt(xZScale), random.nextInt(2) - random.nextInt(2), random.nextInt(xZScale) - random.nextInt(xZScale));
            if (state.canSurvive(this.level(), pos) && this.level().getBlockState(pos).isAir())
                this.setBlockToFlower(state, pos);
        }
        this.gameEvent(GameEvent.BLOCK_PLACE, this);
    }

    public void setBlockToFlower(BlockState state, BlockPos pos) {
        if (this.level().isClientSide) return;
        ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
        if (state.getBlock() == BovineBlocks.CUSTOM_FLOWER.get() && this.getFlowerCowType().configuration().getFlower().getFlowerType().isPresent()) {
            this.level().setBlock(pos, state, 3);
            BlockEntity blockEntity = this.level().getBlockEntity(pos);
            if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                customFlowerBlockEntity.setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(this.getFlowerCowType().configuration().getFlower().getFlowerType().get()).toString());
                customFlowerBlockEntity.setChanged();
            }
        } else {
            this.level().setBlock(pos, state, 3);
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (this.getStandingStillForBeeTicks() > 0) {
            return super.getDimensions(pose).scale(1.0F, 0.7F);
        }
        return super.getDimensions(pose);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getTicksUntilFlowers() > 0)
            this.setTicksUntilFlowers(this.getTicksUntilFlowers() - 1);
        else if (this.getTimesPollinated() > 2 && this.getTicksUntilFlowers() == 0 && (this.level().getBlockState(this.blockPosition()).is(BlockTags.REPLACEABLE) || this.level().getBlockState(this.blockPosition()).is(BlockTags.DIRT) || this.level().getBlockState(this.blockPosition()).is(Blocks.FARMLAND))) {
            this.spreadFlowers(false);
            this.setPollinatedResetTicks(0);
            this.setTimesPollinated(0);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isBaby()) {
            if (itemStack.is(Items.BONE_MEAL)) {
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                if (!this.level().isClientSide) {
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.position().x(), this.position().y() + 1.6D, this.position().z(), 8, 0.5, 0.1, 0.4, 0.0);
                }
                this.spreadFlowers(true);
                this.playSound(BovineSoundEvents.MOOBLOOM_EAT.get(), 1.0f, (random.nextFloat() * 0.4F) + 0.8F);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            } else if (itemStack.is(Items.BOWL)) {
                ItemStack itemStack2;
                itemStack2 = new ItemStack(BovineItems.NECTAR_BOWL.get());
                if (this.getFlowerCowType().configuration().getNectarEffectInstance().isPresent()) {
                    NectarBowlItem.saveMobEffect(itemStack2, this.getFlowerCowType().configuration().getNectarEffectInstance().get().getEffect(), this.getFlowerCowType().configuration().getNectarEffectInstance().get().getDuration());
                } else if (this.getFlowerCowType().configuration().getFlower().blockState().isPresent() && this.getFlowerCowType().configuration().getFlower().blockState().get().getBlock() instanceof FlowerBlock) {
                    NectarBowlItem.saveMobEffect(itemStack2, ((FlowerBlock)this.getFlowerCowType().configuration().getFlower().blockState().get().getBlock()).getSuspiciousEffect(), 600);
                } else {
                    return InteractionResult.PASS;
                }

                NectarBowlItem.saveMoobloomTypeKey(itemStack2, BovineRegistryUtil.getConfiguredCowTypeKey(this.getFlowerCowType()));
                ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                player.setItemInHand(hand, itemStack3);
                this.playSound(BovineSoundEvents.MOOBLOOM_MILK.get(), 1.0f, 1.0f);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> chooseBabyType(LevelAccessor level, FlowerCow otherParent, FlowerCow child) {
        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> eligibleCowTypes = new ArrayList<>();
        boolean bl = false;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream().filter(type -> type.configuration() instanceof FlowerCowConfiguration).toList()) {
            ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> flowerCowType = (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType;
            if (flowerCowType.configuration().getBreedingConditions().isEmpty()) continue;
            var conditions = flowerCowType.configuration().getBreedingConditions().get();

            if (conditions.getCondition().isPresent() && conditions.getOtherCondition().isPresent() && conditions.getCondition().get().test(this) && conditions.getOtherCondition().get().test(otherParent))
                eligibleCowTypes.add(flowerCowType);
            else if (conditions.getOtherCondition().isEmpty() && conditions.getCondition().isPresent() && conditions.getCondition().get().test(this))
                eligibleCowTypes.add(flowerCowType);
            else if (conditions.getCondition().isEmpty() && conditions.getOtherCondition().isPresent() && conditions.getOtherCondition().get().test(otherParent)) {
                bl = true;
                eligibleCowTypes.add(flowerCowType);
            } else if (conditions.getCondition().isEmpty() && conditions.getOtherCondition().isEmpty() && testBreedingBlocks(flowerCowType.configuration(), level())) {
                eligibleCowTypes.add(flowerCowType);
            }
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = this.getRandom().nextInt(eligibleCowTypes.size());
            var randomType = eligibleCowTypes.get(random);
            if (!bl && randomType.configuration().getBreedingConditions().isPresent() && randomType.configuration().getBreedingConditions().get().getCondition().isPresent())
                randomType.configuration().getBreedingConditions().get().getCondition().get().returnCowFeedback(this, new BloomParticleOptions(randomType.configuration().getColor()));
            else if (bl && randomType.configuration().getBreedingConditions().isPresent() && randomType.configuration().getBreedingConditions().get().getOtherCondition().isPresent())
                randomType.configuration().getBreedingConditions().get().getCondition().get().returnCowFeedback(otherParent, new BloomParticleOptions(randomType.configuration().getColor()));
            else
                spawnParticleToBreedPosition(randomType.configuration(), level());

            if (this.getLoveCause() != null && !BovineRegistryUtil.getConfiguredCowTypeKey(randomType).equals(BovineRegistryUtil.getConfiguredCowTypeKey(this.getFlowerCowType())) && !BovineRegistryUtil.getConfiguredCowTypeKey(randomType).equals(BovineRegistryUtil.getConfiguredCowTypeKey(otherParent.getFlowerCowType()))) {
                BovineCriteriaTriggers.MUTATION.trigger(this.getLoveCause(), this, otherParent, child, BovineRegistryUtil.getConfiguredCowTypeKey(randomType));
            }
            return randomType;
        }

        if (!otherParent.getFlowerCowType().equals(this.getFlowerCowType()) && this.getRandom().nextBoolean())
            return otherParent.getFlowerCowType();

        return this.getFlowerCowType();
    }


    public void spawnParticleToBreedPosition(FlowerCowConfiguration configuration, LevelAccessor level) {
        Optional<BreedingConditionConfiguration> breedingCondition = configuration.getBreedingConditions();
        if (breedingCondition.isEmpty()) return;
        double radius = breedingCondition.get().getRadius();
        Map<BlockState, BlockPos> stateMap = new HashMap<>();

        AABB box = new AABB(this.blockPosition()).move(0, radius - 1, 0).inflate(radius);
        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            BlockState state = level().getBlockState(pos);

            breedingCondition.get().getBlockPredicates().forEach(blockPredicate -> {
                if (blockPredicate.operation() != BreedingConditionConfiguration.PredicateOperation.NOT && (blockPredicate.blocks().isPresent() && blockPredicate.blocks().get().contains(state.getBlock()) || blockPredicate.states().isPresent() && blockPredicate.states().get().contains(state)) && (!stateMap.containsKey(state) || pos.distSqr(this.blockPosition()) < stateMap.get(state).distSqr(this.blockPosition()))) {
                    stateMap.put(state, pos.immutable());
                }
            });

            if (breedingCondition.get().shouldIncludeAssociatedBlocks()) {
                if (configuration.getFlower().blockState().isPresent() && (state.is(configuration.getFlower().blockState().get().getBlock()) || state.getBlock() instanceof FlowerPotBlock && ((FlowerPotBlock)state.getBlock()).getContent() == configuration.getFlower().blockState().get().getBlock())) {
                    stateMap.clear();
                    stateMap.put(state, pos.immutable());
                    break;
                } else if (configuration.getFlower().getFlowerType().isPresent() &&
                        (state.is(BovineBlocks.CUSTOM_FLOWER.get()) && level().getBlockEntity(pos) instanceof CustomFlowerBlockEntity flowerBlockEntity && flowerBlockEntity.getFlowerType() == configuration.getFlower().getFlowerType().get() ||
                                state.is(BovineBlocks.POTTED_CUSTOM_FLOWER.get()) && level().getBlockEntity(pos) instanceof CustomFlowerPotBlockEntity flowerPotBlockEntity && flowerPotBlockEntity.getFlowerType() == configuration.getFlower().getFlowerType().get())) {
                    stateMap.clear();
                    stateMap.put(state, pos.immutable());
                    break;
                }
            }
        }

        stateMap.forEach((state, pos) -> {
            VoxelShape shape = state.getShape(level(), pos);
            if (shape.isEmpty()) return;
            AABB blockBox = shape.bounds();
            createParticleTrail(blockBox.getCenter().add(new Vec3(pos.getX(), pos.getY(), pos.getZ())), new BloomParticleOptions(configuration.getColor()));
        });
    }

    public void createParticleTrail(Vec3 pos, ParticleOptions options) {
        double value = (1 - (1 / (pos.distanceTo(this.position()) + 1))) / 4;

        for (double d = 0.0; d < 1.0; d += value) {
            ((ServerLevel)this.level()).sendParticles(options, Mth.lerp(d, pos.x(), this.position().x()), Mth.lerp(d, pos.y(), this.position().y()), Mth.lerp(d, pos.z(), this.position().z()), 1, 0.05, 0.05,  0.05, 0.01);
        }
    }

    public boolean testBreedingBlocks(FlowerCowConfiguration configuration, LevelAccessor level) {
        Optional<BreedingConditionConfiguration> breedingCondition = configuration.getBreedingConditions();

        if (breedingCondition.isEmpty() || breedingCondition.get().getBlockPredicates().isEmpty() && !breedingCondition.get().shouldIncludeAssociatedBlocks())
            return false;

        HashMap<BreedingConditionConfiguration.BlockPredicate, Set<BlockState>> predicateValues = new HashMap<>();
        breedingCondition.get().getBlockPredicates().forEach(blockPredicate -> predicateValues.put(blockPredicate, new HashSet<>()));

        double radius = breedingCondition.get().getRadius();
        boolean associatedBlockFound = false;

        AABB box = new AABB(this.blockPosition()).move(0, radius - 2, 0).inflate(radius - 1);
        for (BlockPos pos : BlockPos.betweenClosed((int) box.minX, (int) box.minY, (int) box.minZ, (int) box.maxX, (int) box.maxY, (int) box.maxZ)) {
            BlockState state = level().getBlockState(pos);

            for (Map.Entry<BreedingConditionConfiguration.BlockPredicate, Set<BlockState>> entry : predicateValues.entrySet()) {
                if (!entry.getValue().contains(state) && (entry.getKey().blocks().isPresent() && entry.getKey().blocks().get().contains(state.getBlock()) || entry.getKey().states().isPresent() && entry.getKey().states().get().contains(state))) {
                    entry.getValue().add(state);
                    predicateValues.put(entry.getKey(), entry.getValue());
                }
            }
            if (breedingCondition.get().shouldIncludeAssociatedBlocks()) {
                if (configuration.getFlower().blockState().isPresent() && (state.is(configuration.getFlower().blockState().get().getBlock()) || state.getBlock() instanceof FlowerPotBlock && ((FlowerPotBlock)state.getBlock()).getContent() == configuration.getFlower().blockState().get().getBlock())) {
                    associatedBlockFound = true;
                    break;
                } else if (configuration.getFlower().getFlowerType().isPresent() &&
                        (state.is(BovineBlocks.CUSTOM_FLOWER.get()) && level().getBlockEntity(pos) instanceof CustomFlowerBlockEntity flowerBlockEntity && flowerBlockEntity.getFlowerType() == configuration.getFlower().getFlowerType().get() ||
                        state.is(BovineBlocks.POTTED_CUSTOM_FLOWER.get()) && level().getBlockEntity(pos) instanceof CustomFlowerPotBlockEntity flowerPotBlockEntity && flowerPotBlockEntity.getFlowerType() == configuration.getFlower().getFlowerType().get())) {
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

    @Override
    public FlowerCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        FlowerCow flowerCow = BovineEntityTypes.MOOBLOOM.get().create(serverLevel);
        flowerCow.setFlowerType(this.chooseBabyType(serverLevel, (FlowerCow)ageableMob, flowerCow), serverLevel);
        return flowerCow;
    }

    public boolean xplatformReadyForShearing() {
        return this.isAlive() && !this.isBaby() && this.entityData.get(ALLOW_SHEARING);
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getFlowerCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(ResourceLocation.tryParse(getTypeId())) && this.type != null && this.type.configuration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(this.entityData.get(TYPE_ID)), BovineCowTypes.FLOWER_COW_TYPE.get()).configuration()) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(getTypeId()), BovineCowTypes.FLOWER_COW_TYPE.get());
                return this.type;
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(ResourceLocation.tryParse(getTypeId()))) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(ResourceLocation.tryParse(getTypeId()), BovineCowTypes.FLOWER_COW_TYPE.get());
                return this.type;
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE.get());
            BovinesAndButtercups.LOG.warn("Could not find type '{}' from moobloom at {}. Setting type to 'bovinesandbuttercups:missing_moobloom'.", ResourceLocation.tryParse(getTypeId()), position());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE.get());
            BovinesAndButtercups.LOG.warn("Could not get type '{}' from moobloom at {}. Setting type to 'bovinesandbuttercups:missing_moobloom'. {}", ResourceLocation.tryParse(getTypeId()), position(), e.getMessage());
            return this.type;
        }
    }

    public String getTypeId() {
        return this.entityData.get(TYPE_ID);
    }

    public String getPreviousTypeId() {
        return this.entityData.get(PREVIOUS_TYPE_ID);
    }

    public void setPreviousTypeId(String value) {
        this.entityData.set(PREVIOUS_TYPE_ID, value);
    }

    public void setFlowerType(String value) {
        this.entityData.set(TYPE_ID, value);
        this.getFlowerCowType();
    }

    public void setFlowerType(ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> value, LevelAccessor level) {
        this.entityData.set(TYPE_ID, BovineRegistryUtil.getConfiguredCowTypeKey(value).toString());
        this.getFlowerCowType();
    }

    public int getTimesPollinated() {
        return this.entityData.get(TIMES_POLLINATED);
    }

    public void setTimesPollinated(int value) {
        this.entityData.set(TIMES_POLLINATED, value);
    }

    public int getPollinatedResetTicks() {
        return this.entityData.get(POLLINATED_RESET_TICKS);
    }

    public void setPollinatedResetTicks(int value) {
        this.entityData.set(POLLINATED_RESET_TICKS, value);
    }

    public int getTicksUntilFlowers() {
        return this.entityData.get(TICKS_UNTIL_FLOWERS);
    }

    public void setTicksUntilFlowers(int value) {
        this.entityData.set(TICKS_UNTIL_FLOWERS, value);
    }

    public int getStandingStillForBeeTicks() {
        return this.entityData.get(STANDING_STILL_FOR_BEE_TICKS);
    }

    public void setStandingStillForBeeTicks(int value) {
        this.entityData.set(STANDING_STILL_FOR_BEE_TICKS, value);
    }

    public boolean shouldAllowShearing() {
        return this.entityData.get(ALLOW_SHEARING);
    }

    public void setAllowShearing(boolean value) {
        this.entityData.set(ALLOW_SHEARING, value);
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        if (entityTag == null || !entityTag.contains("Type")) {
            if (getTotalSpawnWeight(level(), this.blockPosition()) > 0) {
                this.setFlowerType(getMoobloomSpawnTypeDependingOnBiome(level(), this.blockPosition(), this.getRandom()), level());
            } else {
                this.setFlowerType(getMoobloomSpawnType(level(), this.getRandom()), level());
            }
        }
        return super.finalizeSpawn(level, difficulty, spawnType, entityData, entityTag);
    }

    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream().filter(configuredCowType -> configuredCowType.configuration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.configuration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getSettings().naturalSpawnWeight() > 0 && configuration.getSettings().biomes().isPresent() && configuration.getSettings().biomes().get().contains(level.registryAccess(), level.getBiome(pos))) {
                totalWeight += configuration.getSettings().naturalSpawnWeight();
            }
        }
        return totalWeight;
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getMoobloomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream().filter(configuredCowType -> configuredCowType.configuration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.configuration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getSettings().naturalSpawnWeight() > 0) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                totalWeight += configuration.getSettings().naturalSpawnWeight();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.get(0);
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> cfc : moobloomList) {
                r -= cfc.configuration().getSettings().naturalSpawnWeight();
                if (r < 0.0) {
                    return cfc;
                }
            }
        }
        return BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE.get());
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getMoobloomSpawnTypeDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();
        int totalWeight = 0;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream().filter(configuredCowType -> configuredCowType.configuration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.configuration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getSettings().naturalSpawnWeight() > 0 && configuration.getSettings().biomes().isPresent() && configuration.getSettings().biomes().get().contains(level.registryAccess(), level.getBiome(pos))) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                totalWeight += configuration.getSettings().naturalSpawnWeight();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.get(0);
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> cfc : moobloomList) {
                r -= cfc.configuration().getSettings().naturalSpawnWeight();
                if (r < 0.0) {
                    return cfc;
                }
            }
        }
        return BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE.get());
    }

    public List<ItemStack> xplatformShear(SoundSource category) {
        // Implemented in FlowerCowFabriclike and FlowerCowForge
        List<ItemStack> stacks = new ArrayList<>();
        this.level().playSound(null, this, BovineSoundEvents.MOOBLOOM_SHEAR.get(), category, 1.0f, 1.0f);
        if (!this.level().isClientSide) {
            ((ServerLevel)this.level()).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.discard();
            Cow cowEntity = EntityType.COW.create(this.level());
            if (cowEntity != null) {
                cowEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                cowEntity.setHealth(this.getHealth());
                cowEntity.yBodyRot = this.yBodyRot;
                if (this.hasCustomName()) {
                    cowEntity.setCustomName(this.getCustomName());
                    cowEntity.setCustomNameVisible(this.isCustomNameVisible());
                }
                if (this.isPersistenceRequired()) {
                    cowEntity.setPersistenceRequired();
                }
                cowEntity.setInvulnerable(this.isInvulnerable());
                this.level().addFreshEntity(cowEntity);
                for (int i = 0; i < 5; ++i) {
                    if (this.getFlowerCowType().configuration().getFlower().blockState().isPresent()) {
                        stacks.add(new ItemStack(this.getFlowerCowType().configuration().getFlower().blockState().get().getBlock()));
                    } else if (this.getFlowerCowType().configuration().getFlower().getFlowerType().isPresent()) {
                        ItemStack itemStack = new ItemStack(BovineItems.CUSTOM_FLOWER.get());
                        CompoundTag compound = new CompoundTag();
                        compound.putString("Type", BovineRegistryUtil.getFlowerTypeKey(this.getFlowerCowType().configuration().getFlower().getFlowerType().get()).toString());
                        itemStack.getOrCreateTag().put("BlockEntityTag", compound);
                        stacks.add(itemStack);
                    }
                }
            }
        }
        return stacks;
    }

    public class LookAtBeeGoal extends Goal {
        public LookAtBeeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return FlowerCow.this.getStandingStillForBeeTicks() > 0;
        }

        @Override
        public void start() {
            FlowerCow.this.getNavigation().stop();
        }
    }
}
