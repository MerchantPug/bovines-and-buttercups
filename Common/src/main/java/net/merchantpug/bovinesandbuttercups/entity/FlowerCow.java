package net.merchantpug.bovinesandbuttercups.entity;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.api.CowTypeConfiguration;
import net.merchantpug.bovinesandbuttercups.block.entity.CustomFlowerBlockEntity;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import net.merchantpug.bovinesandbuttercups.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.mixin.EntityAccessor;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlowerCow extends Cow implements Shearable {
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> PREVIOUS_TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> POLLINATION_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FLOWERS_TO_GENERATE = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANDING_STILL_FOR_BEE_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> type;
    @Nullable public Bee bee;
    private int timeBetweenFlowerPlacement;
    private boolean hasRefreshedDimensionsForLaying;
    @Nullable
    private UUID lastLightningBoltUUID;

    public FlowerCow(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
        this.bee = null;
        this.timeBetweenFlowerPlacement = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_ID, "bovinesandbuttercups:missing_moobloom");
        this.entityData.define(PREVIOUS_TYPE_ID, "");
        this.entityData.define(POLLINATION_TICKS, 0);
        this.entityData.define(FLOWERS_TO_GENERATE, 0);
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
        compound.putString("Type", this.entityData.get(TYPE_ID));
        if (!this.getPreviousTypeId().equals("")) {
            compound.putString("PreviousType", this.entityData.get(PREVIOUS_TYPE_ID));
        }
        compound.putInt("PollinationTicks", this.getPollinationTicks());
        compound.putInt("FlowersToGenerate", this.getFlowersToGenerate());
        compound.putInt("TimeBetweenFlowerPlacement", this.timeBetweenFlowerPlacement);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Type")) {
            this.setFlowerType(compound.getString("Type"), this.getLevel());
        }
        if (compound.contains("PreviousType")) {
            this.setPreviousTypeId(compound.getString("PreviousType"));
        }
        if (compound.contains("PollinationTicks", 99)) {
            this.setPollinationTicks(compound.getInt("PollinationTicks"));
        }
        if (compound.contains("FlowersToGenerate", 99)) {
            this.setFlowersToGenerate(compound.getInt("FlowersToGenerate"));
        }
        if (compound.contains("TimeBetweenFlowerPlacement", 99)) {
            this.setFlowersToGenerate(compound.getInt("TimeBetweenFlowerPlacement"));
        }
    }

    public void setBee(@Nullable Bee value) {
        this.bee = value;
    }

    public static boolean canMoobloomSpawn(EntityType<FlowerCow> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, RandomSource random) {
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
                    if (weightedCowType.getConfiguredCowType(this.getLevel()).isEmpty() || !(weightedCowType.getConfiguredCowType(this.getLevel()).get().getConfiguration() instanceof FlowerCowConfiguration))
                        continue;

                    if (weightedCowType.weight() > 0) {
                        compatibleList.add(weightedCowType);
                    }
                }

                if (compatibleList.isEmpty()) {
                    super.thunderHit(level, bolt);
                    return;
                } else if (compatibleList.size() == 1) {
                    this.setFlowerType((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) compatibleList.get(0).getConfiguredCowType(this.getLevel()).get(), this.getLevel());
                } else {
                    for (CowTypeConfiguration.WeightedConfiguredCowType cct : compatibleList) {
                        totalWeight -= cct.weight();
                        if (totalWeight <= 0) {
                            this.setFlowerType((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cct.getConfiguredCowType(this.getLevel()).get(), this.getLevel());
                            break;
                        }
                    }
                }
            } else {
                this.setFlowerType(this.getPreviousTypeId(), this.getLevel());
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
        if (this.getStandingStillForBeeTicks() > 0 && !this.level.isClientSide()) {
            this.setStandingStillForBeeTicks(this.getStandingStillForBeeTicks() - 1);
        }
        if (!this.level.isClientSide() && this.getPollinationTicks() > 0 && this.tickCount % 8 == 0) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + this.getBoundingBox().getYsize(), this.getZ(), 1, 0.3, 0.1, 0.3, 0.0);
        }

        if (!this.level.isClientSide() && this.getFlowersToGenerate() > 0 && this.tickCount % 12 == 0) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(), this.getZ(), 1, 0.3, 0.1, 0.3, 0.0);
        }

        super.tick();
        if (this.getStandingStillForBeeTicks() > 0) {
            if (!hasRefreshedDimensionsForLaying) {
                this.refreshDimensions();
                ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
                hasRefreshedDimensionsForLaying = true;
            }
            if (!this.level.isClientSide() && this.bee != null) {
                this.getLookControl().setLookAt(bee);
            }
        } else if (hasRefreshedDimensionsForLaying) {
            this.refreshDimensions();
            ((EntityAccessor)this).bovinesandbuttercups$setEyeHeight(this.getDimensions(this.getPose()).height * 0.85F);
            hasRefreshedDimensionsForLaying = false;
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

        if (this.timeBetweenFlowerPlacement > 0) {
            this.timeBetweenFlowerPlacement--;
        }

        if (this.getPollinationTicks() > 0 && !this.level.isClientSide()) {
            this.setPollinationTicks(this.getPollinationTicks() - 1);
        }

        if (!this.level.isClientSide() && this.level.getBlockState(this.blockPosition()).isAir() && (this.level.getBlockState(this.blockPosition().below()).is(BlockTags.DIRT) || this.level.getBlockState(this.blockPosition().below()).is(Blocks.FARMLAND)) && this.getFlowersToGenerate() > 0 && this.timeBetweenFlowerPlacement == 0) {
            if (this.getFlowerCowType().getConfiguration().getFlower().blockState().isPresent() && this.getFlowerCowType().getConfiguration().getFlower().blockState().get().canSurvive(this.level, this.blockPosition())) {
                ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.3D, this.blockPosition().getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
                this.level.setBlock(this.blockPosition(), this.getFlowerCowType().getConfiguration().getFlower().blockState().get(), 3);
                this.setFlowersToGenerate(this.getFlowersToGenerate() - 1);
                this.gameEvent(GameEvent.BLOCK_PLACE, this);
                if (this.getFlowersToGenerate() > 0) {
                    this.timeBetweenFlowerPlacement = this.random.nextInt(60, 80);
                }
            } else if (this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).isPresent()) {
                ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.3D, this.blockPosition().getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
                this.level.setBlock(this.blockPosition(), BovineBlocks.CUSTOM_FLOWER.get().defaultBlockState(), 3);
                BlockEntity blockEntity = this.level.getBlockEntity(this.blockPosition());
                if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                    customFlowerBlockEntity.setFlowerTypeName(BovineRegistryUtil.getFlowerTypeKey(this.getLevel(), this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).get()).toString());
                    customFlowerBlockEntity.setChanged();
                }
                this.setFlowersToGenerate(this.getFlowersToGenerate() - 1);
                this.gameEvent(GameEvent.BLOCK_PLACE, this);
                if (this.getFlowersToGenerate() > 0) {
                    this.timeBetweenFlowerPlacement = this.random.nextInt(60, 80);
                }
            }
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.HONEY_BOTTLE) && this.getPollinationTicks() > 0) {
            if (!player.getAbilities().instabuild) {
                ItemStack itemStack2;
                itemStack2 = new ItemStack(Items.GLASS_BOTTLE);
                ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
                player.setItemInHand(hand, itemStack3);
            }
            this.setFlowersToGenerate(12);
            this.setPollinationTicks(0);
            this.playSound(BovineSoundEvents.MOOBLOOM_DRINK.get(), 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (itemStack.is(Items.BOWL) && !this.isBaby()) {
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
        } else if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level.isClientSide) {
                itemStack.hurtAndBreak(1, player, other -> other.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource category) {
        this.level.playSound(null, this, BovineSoundEvents.MOOBLOOM_SHEAR.get(), category, 1.0f, 1.0f);
        if (!this.level.isClientSide) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(0.5), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
            this.discard();
            Cow cowEntity = EntityType.COW.create(this.level);
            if (cowEntity == null) return;
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
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(this.getFlowerCowType().getConfiguration().getFlower().blockState().get().getBlock())));
                } else if (this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).isPresent()) {
                    ItemStack itemStack = new ItemStack(Services.PLATFORM.getCustomFlowerItem());
                    CompoundTag compound = new CompoundTag();
                    compound.putString("Type", BovineRegistryUtil.getFlowerTypeKey(this.getLevel(), this.getFlowerCowType().getConfiguration().getFlower().getFlowerType(this.getLevel()).get()).toString());
                    itemStack.getOrCreateTag().put("BlockEntityTag", compound);
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), itemStack));
                }
            }
        }
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> chooseBabyType(LevelAccessor level, FlowerCow other) {
        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> eligibleCowTypes = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(type -> type.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> flowerCowType = (ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType;
            if (flowerCowType.getConfiguration().getBreedingRequirements().isEmpty()) continue;
            if (new HashSet<>(level.getBlockStates(this.getBoundingBox().inflate(8.0D)).toList()).containsAll(flowerCowType.getConfiguration().getBreedingRequirements().get())) {
                eligibleCowTypes.add(flowerCowType);
            }
        }

        if (!eligibleCowTypes.isEmpty()) {
            int random = this.getRandom().nextInt() % eligibleCowTypes.size();
            return eligibleCowTypes.get(random);
        }

        if (other.getFlowerCowType().equals(this.getFlowerCowType()) && this.getRandom().nextBoolean()) {
            return other.getFlowerCowType();
        }
        return this.getFlowerCowType();
    }

    @Override
    public FlowerCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        FlowerCow flowerCow = Services.PLATFORM.getMoobloomEntity().create(serverLevel);
        flowerCow.setFlowerType(this.chooseBabyType(serverLevel, (FlowerCow)ageableMob), this.getLevel());
        return flowerCow;
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getFlowerCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID))) && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)), BovineCowTypes.FLOWER_COW_TYPE).getConfiguration()) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)), BovineCowTypes.FLOWER_COW_TYPE);
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)))) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)), BovineCowTypes.FLOWER_COW_TYPE);
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
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

    public void setFlowerType(String value, LevelAccessor level) {
        this.entityData.set(TYPE_ID, value);
        try {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(level, ResourceLocation.tryParse(value), BovineCowTypes.FLOWER_COW_TYPE);
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"), BovineCowTypes.FLOWER_COW_TYPE);
        }
    }

    public void setFlowerType(ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> value, LevelAccessor level) {
        this.entityData.set(TYPE_ID, BovineRegistryUtil.getConfiguredCowTypeKey(level, value).toString());
        this.type = value;
    }

    public int getPollinationTicks() {
        return this.entityData.get(POLLINATION_TICKS);
    }

    public void setPollinationTicks(int value) {
        this.entityData.set(POLLINATION_TICKS, value);
    }

    public int getFlowersToGenerate() {
        return this.entityData.get(FLOWERS_TO_GENERATE);
    }

    public void setFlowersToGenerate(int value) {
        this.entityData.set(FLOWERS_TO_GENERATE, value);
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

        Registry<Biome> registry = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.getNaturalSpawnWeight() > 0 && configuration.getBiomeTagKey().isPresent()) {
                TagKey<Biome> tag = configuration.getBiomeTagKey().get();
                var optionalList = registry.getTag(tag);
                if(optionalList.isPresent()) {
                    entryList = optionalList.get();
                }
                if (entryList != null && entryList.contains(level.getBiome(pos))) {
                    totalWeight += configuration.getNaturalSpawnWeight();
                }
            }
        }
        return totalWeight;
    }

    public ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>> getMoobloomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>> moobloomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration)) continue;

            if (flowerCowConfiguration.getNaturalSpawnWeight() > 0) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                totalWeight += flowerCowConfiguration.getNaturalSpawnWeight();
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

        Registry<Biome> registry = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (ConfiguredCowType<?, ?> cowType : BovineRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration)) continue;

            if (flowerCowConfiguration.getNaturalSpawnWeight() > 0 && flowerCowConfiguration.getBiomeTagKey().isPresent()) {
                TagKey<Biome> tag = flowerCowConfiguration.getBiomeTagKey().get();
                var optionalList = registry.getTag(tag);
                if(optionalList.isPresent()) {
                    entryList = optionalList.get();
                }
                if (entryList != null && entryList.contains(level.getBiome(pos))) {
                    moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, CowType<FlowerCowConfiguration>>) cowType);
                    totalWeight += flowerCowConfiguration.getNaturalSpawnWeight();
                }
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
