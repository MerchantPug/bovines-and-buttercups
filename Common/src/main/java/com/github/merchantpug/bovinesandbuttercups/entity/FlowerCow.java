package com.github.merchantpug.bovinesandbuttercups.entity;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.item.NectarBowlItem;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineItems;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineSoundEvents;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class FlowerCow extends Cow implements Shearable {
    private static final EntityDataAccessor<String> TYPE_ID = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> POLLINATION_TICKS = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FLOWERS_TO_GENERATE = SynchedEntityData.defineId(FlowerCow.class, EntityDataSerializers.INT);
    public FlowerCowType type;
    public int standingStillForBeeTicks;
    @Nullable public Bee bee;
    private int timeBetweenFlowerPlacement;

    public FlowerCow(EntityType<? extends FlowerCow> entityType, Level level) {
        super(entityType, level);
        this.standingStillForBeeTicks = 0;
        this.bee = null;
        this.timeBetweenFlowerPlacement = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE_ID, FlowerCowType.MISSING.getResourceLocation().toString());
        this.entityData.define(POLLINATION_TICKS, 0);
        this.entityData.define(FLOWERS_TO_GENERATE, 0);
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(4, new FlowerCow.LookAtBeeGoal());
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
            this.setType(compound.getString("Type"));
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
        if (bee != null) {
            standingStillForBeeTicks = 0;
            bee = null;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        if (bee != null && !bee.isAlive()) {
            standingStillForBeeTicks = 0;
            bee = null;
        }
        if (standingStillForBeeTicks > 0) {
            standingStillForBeeTicks--;
        }
        if (!this.level.isClientSide() && this.getPollinationTicks() > 0 && this.tickCount % 8 == 0) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY(1.1), this.getZ(), 1, 0.3, 0.1, 0.3, 0.0);
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.timeBetweenFlowerPlacement > 0) {
            this.timeBetweenFlowerPlacement--;
        }

        if (this.getPollinationTicks() > 0) {
            this.setPollinationTicks(this.getPollinationTicks() - 1);
        }

        if (this.getFlowerCowType().getFlower() != null && !this.level.isClientSide && this.getFlowerCowType().getFlower().canSurvive(this.level, this.blockPosition()) && this.level.getBlockState(this.blockPosition()).isAir() && this.getFlowersToGenerate() > 0 && this.timeBetweenFlowerPlacement == 0) {
            ((ServerLevel)this.level).sendParticles(ParticleTypes.HAPPY_VILLAGER, this.blockPosition().getX() + 0.5D, this.blockPosition().getY() + 0.3D, this.blockPosition().getZ() + 0.5D, 4, 0.2, 0.1, 0.2, 0.0);
            this.level.setBlock(this.blockPosition(), this.getFlowerCowType().getFlower(), 3);
            this.setFlowersToGenerate(this.getFlowersToGenerate() - 1);
            this.gameEvent(GameEvent.BLOCK_PLACE, this);
            if (this.getFlowersToGenerate() > 0) {
                this.timeBetweenFlowerPlacement = this.random.nextInt(60, 80);
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
            if (this.getFlowerCowType().getMobEffectInstance() != null) {
                NectarBowlItem.saveMobEffect(itemStack2, this.getFlowerCowType().getMobEffectInstance().getEffect(), this.getFlowerCowType().getNectarDuration());
            } else if (this.getFlowerCowType().getFlower() != null && this.getFlowerCowType().getFlower().getBlock() instanceof FlowerBlock) {
                NectarBowlItem.saveMobEffect(itemStack2, ((FlowerBlock)this.getFlowerCowType().getFlower().getBlock()).getSuspiciousStewEffect(), this.getFlowerCowType().getNectarDuration());
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
            if (this.getFlowerCowType().getFlower() == null) return;
            for (int i = 0; i < 5; ++i) {
                this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(1.0), this.getZ(), new ItemStack(this.getFlowerCowType().getFlower().getBlock())));
            }
        }
    }

    public FlowerCowType chooseBabyType(FlowerCow other) {
        FlowerCowType type2 = other.getFlowerCowType();
        FlowerCowType type = this.getFlowerCowType();
        return this.random.nextBoolean() ? type : type2;
    }

    @Override
    public FlowerCow getBreedOffspring(ServerLevel serverWorld, AgeableMob ageableMob) {
        FlowerCow flowerCow = (FlowerCow) BovineEntityTypes.MOOBLOOM.get().create(serverWorld);
        flowerCow.setType(this.chooseBabyType((FlowerCow)ageableMob));
        return flowerCow;
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isBaby();
    }

    public FlowerCowType getFlowerCowType() {
        try {
            if (this.type != null) {
                return this.type;
            } else if (FlowerCowTypeRegistry.contains(ResourceLocation.tryParse(this.entityData.get(TYPE_ID)))) {
                return FlowerCowType.fromName(this.entityData.get(TYPE_ID));
            } else {
                this.type = FlowerCowType.MISSING;
                return FlowerCowType.MISSING;
            }
        } catch (Exception e) {
            Constants.LOG.info(e.toString());
            this.type = FlowerCowType.MISSING;
            return FlowerCowType.MISSING;
        }
    }

    public String getTypeId() {
        return this.entityData.get(TYPE_ID);
    }

    public void setType(String value) {
        this.entityData.set(TYPE_ID, value);
        try {
            this.type = FlowerCowTypeRegistry.get(ResourceLocation.tryParse(value));
        } catch (Exception e) {
            this.type = FlowerCowType.MISSING;
        }
    }

    public void setType(FlowerCowType value) {
        this.entityData.set(TYPE_ID, value.getResourceLocation().toString());
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
        return this.standingStillForBeeTicks;
    }

    public void setStandingStillForBeeTicks(int value) {
        this.standingStillForBeeTicks = value;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        if (getTotalSpawnWeight(level, this.blockPosition()) > 0) {
            this.setType(getAndSetMoobloomSpawnTypeDependingOnBiome(level, level.getRandom()));
        } else {
            this.setType(getAndSetMoobloomSpawnType(level, level.getRandom()));
        }
        return super.finalizeSpawn(level, difficulty, spawnType, entityData, entityTag);
    }

    public static int getTotalSpawnWeight(LevelAccessor world, BlockPos pos) {
        int totalWeight = 0;

        Registry<Biome> registry = world.registryAccess().ownedRegistryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (FlowerCowType flowerCowType : FlowerCowTypeRegistry.values()) {
            if (flowerCowType.getNaturalSpawnWeight() > 0 && (flowerCowType.getBiomeKey() != null || flowerCowType.getBiomeTagKey() != null)) {
                ResourceKey<Biome> biome = flowerCowType.getBiomeKey();
                TagKey<Biome> tag = flowerCowType.getBiomeTagKey();
                if(biome != null) {
                    var entry = registry.getHolder(biome);
                    if(entry.isPresent()) {
                        entryList = HolderSet.direct(entry.get());
                    }
                }
                if(entryList == null) {
                    var optionalList = registry.getTag(tag);
                    if(optionalList.isPresent()) {
                        entryList = optionalList.get();
                    }
                }
                if (entryList != null && entryList.contains(world.getBiome(pos))) {
                    totalWeight += flowerCowType.getNaturalSpawnWeight();
                }
            }
        }
        return totalWeight;
    }

    public FlowerCowType getAndSetMoobloomSpawnType(LevelAccessor level, RandomSource random) {
        int totalWeight = 0;

        List<FlowerCowType> moobloomList = new ArrayList<>();

        for (FlowerCowType flowerCowType : FlowerCowTypeRegistry.values()) {
            if (flowerCowType.getNaturalSpawnWeight() > 0) {
                totalWeight += flowerCowType.getNaturalSpawnWeight();
                moobloomList.add(flowerCowType);
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * totalWeight; index < moobloomList.size() - 1; ++index) {
            r -= moobloomList.get(index).getNaturalSpawnWeight();
            if (r <= 0.0) break;
        }
        return moobloomList.get(index);
    }

    public FlowerCowType getAndSetMoobloomSpawnTypeDependingOnBiome(LevelAccessor level, RandomSource random) {
        List<FlowerCowType> moobloomList = new ArrayList<>();

        Registry<Biome> registry = level.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY);

        HolderSet<Biome> entryList = null;

        for (FlowerCowType flowerCowType : FlowerCowTypeRegistry.values()) {
            if (flowerCowType.getNaturalSpawnWeight() > 0 && (flowerCowType.getBiomeKey() != null || flowerCowType.getBiomeTagKey() != null)) {
                ResourceKey<Biome> biome = flowerCowType.getBiomeKey();
                TagKey<Biome> tag = flowerCowType.getBiomeTagKey();
                if(biome != null) {
                    var entry = registry.getHolder(biome);
                    if(entry.isPresent()) {
                        entryList = HolderSet.direct(entry.get());
                    }
                }
                if(entryList == null) {
                    var optionalList = registry.getTag(tag);
                    if(optionalList.isPresent()) {
                        entryList = optionalList.get();
                    }
                }
                if (entryList != null && entryList.contains(level.getBiome(this.blockPosition()))) {
                    moobloomList.add(flowerCowType);
                }
            }
        }

        int index = 0;
        for (double r = random.nextDouble() * getTotalSpawnWeight(level, this.blockPosition()); index < moobloomList.size() - 1; ++index) {
            r -= moobloomList.get(index).getNaturalSpawnWeight();
            if (r <= 0.0) break;
        }
        return moobloomList.get(index);
    }

    public class LookAtBeeGoal extends Goal {
        public LookAtBeeGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return FlowerCow.this.standingStillForBeeTicks > 0 && FlowerCow.this.bee != null;
        }

        @Override
        public void start() {
            FlowerCow.this.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (FlowerCow.this.bee != null) {
                FlowerCow.this.getLookControl().setLookAt(bee, 15.0f, 30.0f);
            }
        }
    }
}
