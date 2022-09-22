package com.github.merchantpug.bovinesandbuttercups.entity;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import com.github.merchantpug.bovinesandbuttercups.block.entity.CustomFlowerBlockEntity;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowBreedingRequirements;
import com.github.merchantpug.bovinesandbuttercups.data.entity.FlowerCowConfiguration;
import com.github.merchantpug.bovinesandbuttercups.item.NectarBowlItem;
import com.github.merchantpug.bovinesandbuttercups.mixin.EntityAccessor;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineBlocks;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineItems;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineSoundEvents;
import com.github.merchantpug.bovinesandbuttercups.util.ConfiguredCowTypeRegistryUtil;
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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlowerCow extends Cow implements Shearable {
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> POLLINATION_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FLOWERS_TO_GENERATE = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> STANDING_STILL_FOR_BEE_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    public ConfiguredCowType<?, ?> type;
    @Nullable public Bee bee;
    private int timeBetweenFlowerPlacement;
    private boolean hasRefreshedDimensionsForLaying;

    public FlowerCow(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
        this.bee = null;
        this.timeBetweenFlowerPlacement = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_ID, "bovinesandbuttercups:missing_moobloom");
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
        compound.putInt("PollinationTicks", this.getPollinationTicks());
        compound.putInt("FlowersToGenerate", this.getFlowersToGenerate());
        compound.putInt("TimeBetweenFlowerPlacement", this.timeBetweenFlowerPlacement);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Type")) {
            this.setFlowerCowType(compound.getString("Type"), this.getLevel());
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

        if (!this.level.isClientSide() && this.level.getBlockState(this.blockPosition()).isAir() && this.getFlowersToGenerate() > 0 && this.timeBetweenFlowerPlacement == 0) {
            if (this.getFlowerCowType().getConfiguration().flower().blockState().isPresent() && this.getFlowerCowType().getConfiguration().flower().blockState().get().canSurvive(this.level, this.blockPosition())) {
                ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.3D, this.blockPosition().getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
                this.level.setBlock(this.blockPosition(), this.getFlowerCowType().getConfiguration().flower().blockState().get(), 3);
                this.setFlowersToGenerate(this.getFlowersToGenerate() - 1);
                this.gameEvent(GameEvent.BLOCK_PLACE, this);
                if (this.getFlowersToGenerate() > 0) {
                    this.timeBetweenFlowerPlacement = this.random.nextInt(60, 80);
                }
            } else if (this.getFlowerCowType().getConfiguration().flower().modelLocation().isPresent() && this.getFlowerCowType().getConfiguration().flower().withFlowerBlock()) {
                ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.3D, this.blockPosition().getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
                this.level.setBlock(this.blockPosition(), BovineBlocks.CUSTOM_FLOWER.get().defaultBlockState(), 3);
                BlockEntity blockEntity = this.level.getBlockEntity(this.blockPosition());
                if (blockEntity instanceof CustomFlowerBlockEntity customFlowerBlockEntity) {
                    customFlowerBlockEntity.setFlowerTypeName(this.getFlowerCowType().getConfiguration().flower().modelLocation().toString());
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
    public InteractionResult mobInteract(Player player2, InteractionHand hand) {
        ItemStack itemStack = player2.getItemInHand(hand);
        if (itemStack.is(Items.HONEY_BOTTLE) && this.getPollinationTicks() > 0) {
            ItemStack itemStack2;
            itemStack2 = new ItemStack(Items.GLASS_BOTTLE);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player2, itemStack2, false);
            player2.setItemInHand(hand, itemStack3);
            this.setFlowersToGenerate(12);
            this.setPollinationTicks(0);
            this.playSound(SoundEvents.HONEY_DRINK, 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (itemStack.is(Items.BOWL) && !this.isBaby()) {
            ItemStack itemStack2;
            itemStack2 = new ItemStack(BovineItems.NECTAR_BOWL.get());
            if (this.getFlowerCowType().getConfiguration().nectarEffectInstance().isPresent()) {
                NectarBowlItem.saveMobEffect(itemStack2, this.getFlowerCowType().getConfiguration().nectarEffectInstance().get().getEffect(), this.getFlowerCowType().getConfiguration().nectarEffectInstance().get().getDuration());
            } else if (this.getFlowerCowType().getConfiguration().flower().blockState().isPresent() && this.getFlowerCowType().getConfiguration().flower().blockState().get().getBlock() instanceof FlowerBlock) {
                NectarBowlItem.saveMobEffect(itemStack2, ((FlowerBlock)this.getFlowerCowType().getConfiguration().flower().blockState().get().getBlock()).getSuspiciousStewEffect(), 600);
            } else {
                return InteractionResult.PASS;
            }

            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player2, itemStack2, false);
            player2.setItemInHand(hand, itemStack3);
            this.playSound(BovineSoundEvents.MOOBLOOM_MILK.get(), 1.0f, 1.0f);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player2);
            if (!this.level.isClientSide) {
                itemStack.hurtAndBreak(1, player2, player -> player.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player2, hand);
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
                if (this.getFlowerCowType().getConfiguration().flower().blockState().isPresent()) {
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(this.getFlowerCowType().getConfiguration().flower().blockState().get().getBlock())));
                } else if (this.getFlowerCowType().getConfiguration().flower().withFlowerBlock() && this.getFlowerCowType().getConfiguration().flower().modelLocation().isPresent()) {
                    ItemStack itemStack = new ItemStack(Services.PLATFORM.getCustomFlowerItem());
                    CompoundTag compound = new CompoundTag();
                    compound.putString("Type", this.getFlowerCowType().getConfiguration().flower().key().toString());
                    itemStack.getOrCreateTag().put("BlockEntityTag", compound);
                    this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), itemStack));
                }
            }
        }
    }

    public ConfiguredCowType<FlowerCowConfiguration, ?> chooseBabyType(LevelAccessor level, FlowerCow other) {
        HashMap<ConfiguredCowType<FlowerCowConfiguration, ?>, Double> eligbleTypeMap = new HashMap<>();
        double chancesTotal = 1.0F;
        for (ConfiguredCowType<?, ?> cowTypeInstance : ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(this.getLevel()).filter(cowTypeInstance -> cowTypeInstance.getConfiguration() instanceof FlowerCowConfiguration && ((FlowerCowConfiguration)cowTypeInstance.getConfiguration()).breedingRequirements().isPresent()).toList()) {
            FlowerCowConfiguration flowerCowType = (FlowerCowConfiguration)cowTypeInstance.getConfiguration();
            for (FlowerCowBreedingRequirements breedingRequirements : flowerCowType.breedingRequirements().stream().filter(br -> br.doesApply(level, this.getFlowerCowType(), other.getFlowerCowType())).toList()) {
               double chance = breedingRequirements.isBoosted(this.getFlowerCowType().getConfiguration(), other.getFlowerCowType().getConfiguration()) ? breedingRequirements.boostedChance() : breedingRequirements.chance();
               eligbleTypeMap.put((ConfiguredCowType<FlowerCowConfiguration, ?>) cowTypeInstance, chance);
               chancesTotal -= chance;
            }
        }

        eligbleTypeMap.put(this.getFlowerCowType(), chancesTotal / 2);
        if (!this.getFlowerCowType().equals(other.getFlowerCowType())) {
            eligbleTypeMap.put(other.getFlowerCowType(), chancesTotal / 2);
        }

        ConfiguredCowType<FlowerCowConfiguration, ?> babyType = this.getFlowerCowType();

        for (Map.Entry<ConfiguredCowType<FlowerCowConfiguration, ?>, Double> map : eligbleTypeMap.entrySet()) {
            if (this.random.nextDouble() < map.getValue()) {
                babyType = map.getKey();
            }
        }
        return babyType;
    }

    @Override
    public FlowerCow getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        FlowerCow flowerCow = Services.PLATFORM.getMoobloomEntity().create(serverLevel);
        flowerCow.setFlowerCowType(this.chooseBabyType(serverLevel, (FlowerCow)ageableMob), this.getLevel());
        return flowerCow;
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }

    public ConfiguredCowType<FlowerCowConfiguration, ?> getFlowerCowType() {
        try {
            if (ConfiguredCowTypeRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID))) && this.type.getConfiguration() != ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID))).getConfiguration()) {
                ConfiguredCowType<?, ?> instance = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)));
                if (instance.getConfiguration() instanceof FlowerCowConfiguration) {
                    return (ConfiguredCowType<FlowerCowConfiguration, ?>)instance;
                }
            } else if (this.type != null) {
                return (ConfiguredCowType<FlowerCowConfiguration, ?>) this.type;
            } else if (ConfiguredCowTypeRegistryUtil.isConfiguredCowTypeInRegistry(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)))) {
                ConfiguredCowType<?, ?> instance = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), ResourceLocation.tryParse(this.entityData.get(TYPE_ID)));
                if (instance.getConfiguration() instanceof FlowerCowConfiguration) {
                    return (ConfiguredCowType<FlowerCowConfiguration, ?>) instance;
                }
            }
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"));
            return (ConfiguredCowType<FlowerCowConfiguration, ?>) ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"));
        } catch (Exception e) {
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"));
            return (ConfiguredCowType<FlowerCowConfiguration, ?>) ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(this.getLevel(), BovinesAndButtercups.asResource("missing_moobloom"));
        }
    }

    public String getTypeId() {
        return this.entityData.get(TYPE_ID);
    }

    public void setFlowerCowType(String value, LevelAccessor level) {
        this.entityData.set(TYPE_ID, value);
        try {
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(level, ResourceLocation.tryParse(value));
        } catch (Exception e) {
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"));
        }
    }

    public void setFlowerCowType(ConfiguredCowType<?, ?> value, LevelAccessor level) {
        this.entityData.set(TYPE_ID, ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(level, value).toString());
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
            this.setFlowerCowType(getMoobloomSpawnTypeDependingOnBiome(level, this.blockPosition(), level.getRandom()), level);
        } else {
            this.setFlowerCowType(getMoobloomSpawnType(level, level.getRandom()), level);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, entityData, entityTag);
    }

    public static int getTotalSpawnWeight(LevelAccessor level, BlockPos pos) {
        int totalWeight = 0;

        Registry<Biome> registry = level.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (ConfiguredCowType<?, ?> cowType : ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration configuration)) continue;

            if (configuration.naturalSpawnWeight() > 0 && configuration.biomeTagKey().isPresent()) {
                TagKey<Biome> tag = configuration.biomeTagKey().get();
                var optionalList = registry.getTag(tag);
                if(optionalList.isPresent()) {
                    entryList = optionalList.get();
                }
                if (entryList != null && entryList.contains(level.getBiome(pos))) {
                    totalWeight += configuration.naturalSpawnWeight();
                }
            }
        }
        return totalWeight;
    }

    public ConfiguredCowType<?, ?> getMoobloomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<ConfiguredCowType<FlowerCowConfiguration, ?>> moobloomList = new ArrayList<>();

        for (ConfiguredCowType<?, ?> cowType : ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration)) continue;

            if (flowerCowConfiguration.naturalSpawnWeight() > 0) {
                moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, ?>) cowType);
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * totalWeight; index < moobloomList.size() - 1; ++index) {
            r -= moobloomList.get(index).getConfiguration().naturalSpawnWeight();
            if (r <= 0.0) break;
        }
        if (!moobloomList.isEmpty()) {
            return moobloomList.get(index);
        } else {
            return ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"));
        }
    }

    public ConfiguredCowType<?, ?> getMoobloomSpawnTypeDependingOnBiome(LevelAccessor level, BlockPos pos, RandomSource random) {
        List<ConfiguredCowType<FlowerCowConfiguration, ?>> moobloomList = new ArrayList<>();

        Registry<Biome> registry = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (ConfiguredCowType<?, ?> cowType : ConfiguredCowTypeRegistryUtil.configuredCowTypeStream(level).filter(configuredCowType -> configuredCowType.getConfiguration() instanceof FlowerCowConfiguration).toList()) {
            if (!(cowType.getConfiguration() instanceof FlowerCowConfiguration flowerCowConfiguration)) continue;

            if (flowerCowConfiguration.naturalSpawnWeight() > 0 && flowerCowConfiguration.biomeTagKey().isPresent()) {
                TagKey<Biome> tag = flowerCowConfiguration.biomeTagKey().get();
                var optionalList = registry.getTag(tag);
                if(optionalList.isPresent()) {
                    entryList = optionalList.get();
                }
                if (entryList != null && entryList.contains(level.getBiome(pos))) {
                    moobloomList.add((ConfiguredCowType<FlowerCowConfiguration, ?>) cowType);
                }
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * getTotalSpawnWeight(level, pos); index < moobloomList.size() - 1; ++index) {
            r -= moobloomList.get(index).getConfiguration().naturalSpawnWeight();
            if (r <= 0.0) break;
        }
        if (!moobloomList.isEmpty()) {
            return moobloomList.get(index);
        } else {
            return ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(level, BovinesAndButtercups.asResource("missing_moobloom"));
        }
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
