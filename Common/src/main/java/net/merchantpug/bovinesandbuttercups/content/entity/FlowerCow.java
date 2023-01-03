package net.merchantpug.bovinesandbuttercups.content.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.content.block.CustomFlowerBlock;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.entity.BreedingConditionConfiguration;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.mixin.EntityAccessor;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlowerCow extends Cow {
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PREVIOUS_TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> POLLINATED_RESET_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TICKS_UNTIL_FLOWERS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIMES_POLLINATED = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANDING_STILL_FOR_BEE_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> type;
    @Nullable public Bee bee;
    private boolean hasRefreshedDimensionsForLaying;
    @Nullable private UUID lastLightningBoltUUID;

    public FlowerCow(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
        this.bee = null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> naturalSpawnType;

        if (getTotalSpawnWeight(this.getLevel(), this.blockPosition()) > 0) {
            naturalSpawnType = getMoobloomSpawnTypeDependingOnBiome(this.getLevel(), this.blockPosition(), this.getRandom());
        } else {
            naturalSpawnType = getMoobloomSpawnType(this.getLevel(), this.getRandom());
        }

        this.entityData.define(TYPE_ID, BovineRegistryUtil.getConfiguredCowTypeKey(this.getLevel(), naturalSpawnType).toString());
        this.entityData.define(PREVIOUS_TYPE_ID, "");
        this.entityData.define(POLLINATED_RESET_TICKS, 0);
        this.entityData.define(TICKS_UNTIL_FLOWERS, 0);
        this.entityData.define(TIMES_POLLINATED, 0);
        this.entityData.define(STANDING_STILL_FOR_BEE_TICKS, 0);
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
                if (this.getFlowerCowType().getConfiguration().getThunderConversionTypes().isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                }
                this.setPreviousTypeId(this.getTypeId());

                List<CowTypeConfiguration.WeightedConfiguredCowType> compatibleList = new ArrayList<>();
                int totalWeight = 0;

                for (CowTypeConfiguration.WeightedConfiguredCowType weightedCowType : this.getFlowerCowType().getConfiguration().getThunderConversionTypes().get()) {
                    if (weightedCowType.getConfiguredCowType(level).isEmpty()) {
                        BovinesAndButtercups.LOG.warn("Lightning struck moobloom at {} tried to get thunder conversion type '{}' that does not exist. (Skipping).", this.position(), weightedCowType.configuredCowTypeResource());
                        continue;
                    } else if (!(weightedCowType.getConfiguredCowType(level).get().getConfiguration() instanceof FlowerCowConfiguration)) {
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
        if (bee != null && !this.level.isClientSide()) {
            this.setStandingStillForBeeTicks(0);
            bee = null;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (bee != null && !bee.isAlive() && !this.level.isClientSide()) {
            this.setStandingStillForBeeTicks(0);
            bee = null;
        }
        if (this.getStandingStillForBeeTicks() > 0 && !this.level.isClientSide())
            this.setStandingStillForBeeTicks(this.getStandingStillForBeeTicks() - 1);

        super.tick();
        if (this.getPollinatedResetTicks() > 0)
            this.setPollinatedResetTicks(this.getPollinatedResetTicks() - 1);
        else if (this.getPollinatedResetTicks() <= 0 && this.getTimesPollinated() > 0)
            this.setTimesPollinated(0);

        if (this.getTicksUntilFlowers() > 0 && !this.level.isClientSide && this.age % 8 == 0)
            ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.position().x(), this.position().y() + this.getBbHeight(), this.position().z(), 1, 0.3, 0.1, 0.2, 0.0);

        if (this.getStandingStillForBeeTicks() > 0) {
            if (!hasRefreshedDimensionsForLaying) {
                this.refreshDimensions();
                ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
                hasRefreshedDimensionsForLaying = true;
            }
            if (!this.level.isClientSide() && this.bee != null)
                this.getLookControl().setLookAt(bee);
        } else if (hasRefreshedDimensionsForLaying) {
            this.refreshDimensions();
            ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
            hasRefreshedDimensionsForLaying = false;
        }
    }

    public void spreadFlowers(boolean boneMealed) {
        if (this.level.isClientSide) return;

        BlockState state = null;
        if (this.getFlowerCowType().getConfiguration().getFlower().blockState().isPresent())
            state = this.getFlowerCowType().getConfiguration().getFlower().blockState().get().getBlock().defaultBlockState().setValue(CustomFlowerBlock.PERSISTENT, boneMealed);
        else if (this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).isPresent())
            state = BovineBlocks.CUSTOM_FLOWER.get().defaultBlockState().setValue(CustomFlowerBlock.PERSISTENT, boneMealed);

        if (state == null) {
            BovinesAndButtercups.LOG.warn("Moobloom with type '{}' tried to spread flowers without a valid flower type.", BovineRegistryUtil.getConfiguredCowTypeKey(level, getFlowerCowType()));
            return;
        }

        int maxTries = boneMealed ? 16 : 32;
        int xZScale = boneMealed ? 3 : 6;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for(int i = 0; i < maxTries; ++i) {
            pos.setWithOffset(this.blockPosition(), random.nextInt(xZScale) - random.nextInt(xZScale), random.nextInt(2) - random.nextInt(2), random.nextInt(xZScale) - random.nextInt(xZScale));
            if (state.canSurvive(this.level, pos) && this.level.getBlockState(pos).isAir())
                this.setBlockToFlower(state, pos);
        }
        this.gameEvent(GameEvent.BLOCK_PLACE, this);
    }

    public void setBlockToFlower(BlockState state, BlockPos pos) {
        if (this.level.isClientSide) return;
        ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, pos.getX() + 0.5D, pos.getY() + 0.3D, pos.getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
        if (state.getBlock() == BovineBlocks.CUSTOM_FLOWER.get() && this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).isPresent()) {
            this.level.setBlock(pos, state, 3);
            BlockEntity blockEntity = this.level.getBlockEntity(pos);
            if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                customFlowerBlockEntity.setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(this.getLevel(), this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).get()).toString());
                customFlowerBlockEntity.setChanged();
            }
        } else {
            this.level.setBlock(pos, state, 3);
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
        else if (this.getTimesPollinated() > 2 && this.getTicksUntilFlowers() == 0 && (this.level.getBlockState(this.blockPosition()).getMaterial().isReplaceable() || this.level.getBlockState(this.blockPosition()).getMaterial() == Material.PLANT)) {
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
                if (!this.level.isClientSide) {
                    ((ServerLevel) this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.position().x(), this.position().y() + 1.6D, this.position().z(), 8, 0.5, 0.1, 0.4, 0.0);
                }
                this.spreadFlowers(true);
                this.playSound(BovineSoundEvents.MOOBLOOM_EAT.get(), 1.0f, (random.nextFloat() * 0.4F) + 0.8F);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            } else if (itemStack.is(Items.BOWL)) {
                ItemStack itemStack2;
                itemStack2 = new ItemStack(BovineItems.NECTAR_BOWL.get());
                if (this.getFlowerCowType().getConfiguration().getNectarEffectInstance().isPresent()) {
                    NectarBowlItem.saveMobEffect(itemStack2, this.getFlowerCowType().getConfiguration().getNectarEffectInstance().get().getEffect(), this.getFlowerCowType().getConfiguration().getNectarEffectInstance().get().getDuration());
                } else if (this.getFlowerCowType().getConfiguration().getFlower().blockState().isPresent() && this.getFlowerCowType().getConfiguration().getFlower().blockState().get().getBlock() instanceof FlowerBlock) {
                    NectarBowlItem.saveMobEffect(itemStack2, ((FlowerBlock)this.getFlowerCowType().getConfiguration().getFlower().blockState().get().getBlock()).getSuspiciousStewEffect(), 600);
                } else {
                    return InteractionResult.PASS;
                }

                ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                player.setItemInHand(hand, itemStack3);
                this.playSound(BovineSoundEvents.MOOBLOOM_MILK.get(), 1.0f, 1.0f);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
        }
        return super.mobInteract(player, hand);
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> chooseBabyType(LevelAccessor level, FlowerCow otherParent) {
        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(type -> type.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> flowerCowType = (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType;
            if (flowerCowType.getConfiguration().getBreedingConditions().isEmpty()) continue;
            if (flowerCowType.getConfiguration().getBreedingConditions().get().test(this.blockPosition(), level))
                eligibleCowTypes.add(flowerCowType);
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = this.getRandom().nextInt() % eligibleCowTypes.size();
            var randomType = eligibleCowTypes.get(random);
            Optional<BreedingConditionConfiguration> configuration = randomType.getConfiguration().getBreedingConditions();
            if (configuration.isPresent() && configuration.get().getParticleOptions().isPresent()) {
                configuration.get().spawnParticleTrail(this, level);
            }
            return randomType;
        }

        if (!otherParent.getFlowerCowType().equals(this.getFlowerCowType()) && this.getRandom().nextBoolean())
            return otherParent.getFlowerCowType();

        return this.getFlowerCowType();
    }

    @Override
    public FlowerCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        FlowerCow flowerCow = Services.PLATFORM.getMoobloomEntity().create(serverLevel);
        flowerCow.setFlowerType(this.chooseBabyType(serverLevel, (FlowerCow)ageableMob), serverLevel);
        return flowerCow;
    }

    public boolean xplatformReadyForShearing() {
        return this.isAlive() && !this.isBaby();
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getFlowerCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(getTypeId())) && this.type != null && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)), BovineCowTypes.FLOWER_COW_TYPE).getConfiguration()) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(getTypeId()), BovineCowTypes.FLOWER_COW_TYPE);
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(getTypeId()))) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(getTypeId()), BovineCowTypes.FLOWER_COW_TYPE);
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
            BovinesAndButtercups.LOG.warn("Could not find type '{}' from moobloom at {}. Setting type to 'bovinesandbuttercups:missing_moobloom'.", ResourceLocation.tryParse(getTypeId()), position());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
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
        this.entityData.set(TYPE_ID, BovineRegistryUtil.getConfiguredCowTypeKey(level, value).toString());
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

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        if (getTotalSpawnWeight(level, this.blockPosition()) > 0) {
            this.setFlowerType(getMoobloomSpawnTypeDependingOnBiome(level, this.blockPosition(), this.getRandom()), level);
        } else {
            this.setFlowerType(getMoobloomSpawnType(level, this.getRandom()), level);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, entityData, entityTag);
    }

    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomes().isPresent() && configuration.getBiomes().get().contains(level.getBiome(pos))) {
                totalWeight += configuration.getNaturalSpawnWeight();
            }
        }
        return totalWeight;
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getMoobloomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                totalWeight += configuration.getNaturalSpawnWeight();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.get(0);
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> cfc : moobloomList) {
                r -= cfc.getConfiguration().getNaturalSpawnWeight();
                if (r < 0.0) {
                    return cfc;
                }
            }
        }
        return BovineRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getMoobloomSpawnTypeDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();
        int totalWeight = 0;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomes().isPresent() && configuration.getBiomes().get().contains(level.getBiome(pos))) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                totalWeight += configuration.getNaturalSpawnWeight();
            }
        }

        if (moobloomList.size() == 1) {
            return moobloomList.get(0);
        } else if (!moobloomList.isEmpty()) {
            int r = Mth.nextInt(random, 0, totalWeight - 1);
            for (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> cfc : moobloomList) {
                r -= cfc.getConfiguration().getNaturalSpawnWeight();
                if (r < 0.0) {
                    return cfc;
                }
            }
        }
        return BovineRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
    }

    public List<ItemStack> xplatformShear(SoundSource category) {
        // Implemented in FlowerCowFabriclike and FlowerCowForge
        List<ItemStack> stacks = new ArrayList<>();
        this.level.playSound(null, this, BovineSoundEvents.MOOBLOOM_SHEAR.get(), category, 1.0f, 1.0f);
        if (!this.level.isClientSide) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.discard();
            Cow cowEntity = EntityType.COW.create(this.level);
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
                this.level.addFreshEntity(cowEntity);
                for (int i = 0; i < 5; ++i) {
                    if (this.getFlowerCowType().getConfiguration().getFlower().blockState().isPresent()) {
                        stacks.add(new ItemStack(this.getFlowerCowType().getConfiguration().getFlower().blockState().get().getBlock()));
                    } else if (this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).isPresent()) {
                        ItemStack itemStack = new ItemStack(Services.PLATFORM.getCustomFlowerItem());
                        CompoundTag compound = new CompoundTag();
                        compound.putString("Type", BovineRegistryUtil.getFlowerTypeKey(this.getLevel(), this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).get()).toString());
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
