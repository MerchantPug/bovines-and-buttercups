package net.merchantpug.bovinesandbuttercups.entity.goal;

import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.mixin.BeeAccessor;
import net.merchantpug.bovinesandbuttercups.mixin.MobAccessor;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;

public class PollinateFlowerCowGoal extends Bee.BaseBeeGoal {
    private final Bee bee;
    private static final int MIN_POLLINATION_TICKS = 400;
    private static final int MIN_FIND_FLOWER_COW_RETRY_COOLDOWN = 20;
    private static final int MAX_FIND_FLOWER_COW_RETRY_COOLDOWN = 60;
    private static final double ARRIVAL_THRESHOLD = 0.1D;
    private static final int POSITION_CHANGE_CHANCE = 25;
    private static final float SPEED_MODIFIER = 1.3F;
    private static final float HOVER_POS_OFFSET = 0.33333334F;
    private int successfulPollinatingTicks;
    private int lastSoundPlayedTick;
    private boolean pollinating;
    @Nullable
    private Vec3 hoverPos;
    private int pollinatingTicks;
    private static final int MAX_POLLINATING_TICKS = 600;
    private int remainingCooldownBeforeLocatingNewCow;
    private FlowerCow moobloom;

    public PollinateFlowerCowGoal(Bee bee) {
        bee.super();
        this.bee = bee;
        remainingCooldownBeforeLocatingNewCow = Mth.nextInt(bee.getRandom(), MIN_FIND_FLOWER_COW_RETRY_COOLDOWN, MAX_FIND_FLOWER_COW_RETRY_COOLDOWN);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canBeeUse() {
        if (this.remainingCooldownBeforeLocatingNewCow > 0) {
            return false;
        } if (bee.hasNectar()) {
            return false;
        } else if (bee.level.isRaining()) {
            return false;
        } else if (Services.COMPONENT.getMoobloomTarget(bee).isPresent()) {
            Optional<Entity> entity = Optional.ofNullable(((ServerLevel)bee.getLevel()).getEntity(Services.COMPONENT.getMoobloomTarget(bee).get()));
            if (entity.isPresent() && entity.get() instanceof FlowerCow flowerCow) {
                this.moobloom = flowerCow;
                setFlowerCow();
                return true;
            }
            return false;
        } else if (this.moobloom == null && this.bee.hasSavedFlowerPos()) {
            return false;
        } else {
            Optional<FlowerCow> flowerCow = findMoobloom();
            if (flowerCow.isPresent()) {
                this.moobloom = flowerCow.get();
                setFlowerCow();
                return true;
            } else {
                this.remainingCooldownBeforeLocatingNewCow = Mth.nextInt(bee.getRandom(), 20, 60);
                return false;
            }
        }
    }

    private void setFlowerCow() {
        Services.COMPONENT.setMoobloomTarget(bee, moobloom.getUUID());
        moobloom.setStandingStillForBeeTicks(MAX_POLLINATING_TICKS);
        moobloom.setBee(bee);
        bee.setSavedFlowerPos(moobloom.blockPosition());
        ((MobAccessor)this.bee).bovinesandbuttercups$getNavigation().moveTo(moobloom.position().x(), moobloom.position().y() + moobloom.getBoundingBox().getYsize() * 1.5, moobloom.position().z(), 1.0);

    }

    @Override
    public boolean canBeeContinueToUse() {
        if (!this.pollinating) {
            return false;
        } else if (!bee.hasSavedFlowerPos()) {
            return false;
        } else if (bee.level.isRaining()) {
            return false;
        } else if (this.hasPollinatedLongEnough()) {
            return bee.getRandom().nextFloat() < 0.2F;
        } if (bee.tickCount % 20 == 0 && moobloom != null && (!moobloom.isAlive() || moobloom.getLastHurtByMobTimestamp() > moobloom.tickCount - 100)) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            Services.COMPONENT.setMoobloomTarget(bee, null);
            bee.setSavedFlowerPos(null);
            this.moobloom = null;
            return false;
        } else {
            return true;
        }
    }

    private boolean hasPollinatedLongEnough() {
        return this.successfulPollinatingTicks > MIN_POLLINATION_TICKS;
    }

    public boolean isPollinating() {
        return this.pollinating;
    }

    public void stopPollinating() {
        this.pollinating = false;
    }

    public void tickCooldown() {
        if (this.remainingCooldownBeforeLocatingNewCow > 0) {
            --this.remainingCooldownBeforeLocatingNewCow;
        }
    }

    public void start() {
        this.successfulPollinatingTicks = 0;
        this.pollinatingTicks = 0;
        this.lastSoundPlayedTick = 0;
        this.pollinating = true;
        bee.resetTicksWithoutNectarSinceExitingHive();
    }

    public void stop() {
        if (this.hasPollinatedLongEnough()) {
            ((BeeAccessor)bee).bovinesandbuttercups$invokeSetHasNectar(true);
            moobloom.setPollinationTicks(2600);
        }

        this.pollinating = false;
        bee.getNavigation().stop();
        if (this.moobloom != null) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            Services.COMPONENT.setMoobloomTarget(bee, null);
            this.moobloom = null;
        }
        this.remainingCooldownBeforeLocatingNewCow = 200;
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        ++this.pollinatingTicks;
        if (this.pollinatingTicks > MAX_POLLINATING_TICKS) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            bee.setSavedFlowerPos(null);
            Services.COMPONENT.setMoobloomTarget(bee, null);
            this.moobloom = null;
        } else if (Services.COMPONENT.getMoobloomTarget(bee).isPresent() && !bee.level.isClientSide() ){
            Entity entity = ((ServerLevel)bee.level).getEntity(Services.COMPONENT.getMoobloomTarget(bee).get());
            if ((entity instanceof FlowerCow moobloom)) {
                moobloom.setStandingStillForBeeTicks(600);
                moobloom.setBee(this.bee);
                Vec3 vec3 = moobloom.position().add(0.0f, moobloom.getBoundingBox().getYsize() * 1.3, 0.0f);
                if (vec3.distanceTo(bee.position()) > 1.0D) {
                    this.hoverPos = vec3;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = vec3;
                    }

                    boolean flag = bee.position().distanceTo(this.hoverPos) <= ARRIVAL_THRESHOLD;
                    boolean flag1 = true;
                    if (!flag && this.pollinatingTicks > MAX_POLLINATING_TICKS) {
                        moobloom.setStandingStillForBeeTicks(0);
                        moobloom.setBee(null);
                        bee.setSavedFlowerPos(null);
                        Services.COMPONENT.setMoobloomTarget(bee, null);
                        this.moobloom = null;
                    } else {
                        if (flag) {
                            boolean flag2 = bee.getRandom().nextInt(POSITION_CHANGE_CHANCE) == 0;
                            if (flag2) {
                                this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                                bee.getNavigation().stop();
                            } else {
                                flag1 = false;
                            }

                            bee.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }

                        if (flag1) {
                            this.setWantedPos();
                        }

                        ++this.successfulPollinatingTicks;
                        if (bee.getRandom().nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                            this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                            bee.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    private void setWantedPos() {
        bee.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), SPEED_MODIFIER);
    }

    private float getOffset() {
        return (bee.getRandom().nextFloat() * 2.0F - 1.0F) * HOVER_POS_OFFSET;
    }

    private Optional<FlowerCow> findMoobloom() {
        FlowerCow moobloom = this.bee.level.getNearestEntity(FlowerCow.class, TargetingConditions.forNonCombat().selector(entity -> entity.getLastHurtByMobTimestamp() <= entity.tickCount - 100 && entity.level.getBlockState(entity.blockPosition().above(2)).isAir() && !entity.isBaby() && ((FlowerCow)entity).bee == null), null, bee.getX(), bee.getY(), bee.getZ(), bee.getBoundingBox().inflate(6.0F, 4.0, 6.0F));
        if (moobloom != null) {
            return Optional.of(moobloom);
        }
        return Optional.empty();
    }
}
