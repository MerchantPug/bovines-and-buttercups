package net.merchantpug.bovinesandbuttercups.entity.goal;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.mixin.BeeAccessor;
import net.merchantpug.bovinesandbuttercups.mixin.MobAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class MoveToMoobloomGoal extends Bee.BaseBeeGoal {
    int ticks;
    Bee bee;

    public MoveToMoobloomGoal(Bee bee) {
        bee.super();
        this.bee = bee;
        this.ticks = bee.level.random.nextInt(10);
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canBeeUse() {
        return !bee.hasRestriction() && this.shouldMoveToMoobloom() && ((BeeAccess)bee).bovinesandbuttercups$getTargetMoobloom() != null && ((ServerLevel) bee.level).getEntity(((BeeAccess) bee).bovinesandbuttercups$getTargetMoobloom()) != null && !bee.blockPosition().closerToCenterThan(((ServerLevel) bee.level).getEntity(((BeeAccess) bee).bovinesandbuttercups$getTargetMoobloom()).position(), 2);
    }

    @Override
    public boolean canBeeContinueToUse() {
        return this.canBeeUse();
    }

    @Override
    public void start() {
        this.ticks = 0;
        super.start();
    }

    @Override
    public void stop() {
        this.ticks = 0;
        ((MobAccessor)bee).bovinesandbuttercups$getNavigation().stop();
        ((MobAccessor)bee).bovinesandbuttercups$getNavigation().resetMaxVisitedNodesMultiplier();
    }

    @Override
    public void tick() {
        if (((BeeAccess)bee).bovinesandbuttercups$getTargetMoobloom() == null) {
            return;
        }
        Entity entity = ((ServerLevel)bee.level).getEntity(((BeeAccess) bee).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) {
            return;
        }
        ++this.ticks;
        if (this.ticks > this.adjustedTickDelay(600)) {
            ((BeeAccess)bee).bovinesandbuttercups$setTargetMoobloom(null);
            return;
        }
        if (((MobAccessor)bee).bovinesandbuttercups$getNavigation().isInProgress()) {
            return;
        }
        if (!bee.position().closerThan(moobloom.position(), 32)) {
            ((BeeAccess)bee).bovinesandbuttercups$setTargetMoobloom(null);
            return;
        }
        this.startMovingTo(bee, moobloom.position().add(0.0f, moobloom.getBoundingBox().getYsize() * 1.3, 0.0f));
    }

    void startMovingTo(Bee bee, Vec3 pos) {
        int i = 0;
        Vec3 beePos = bee.position();
        double j = pos.y - beePos.y;
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        double m = beePos.distanceToSqr(pos);
        if (m < 15) {
            k = (int) Math.round(m / 2);
            l = (int) Math.round(m / 2);
        }

        Vec3 vec32 = AirRandomPos.getPosTowards(bee, k, l, i, pos, 0.3141592741012573D);
        if (vec32 != null) {
            ((MobAccessor)bee).bovinesandbuttercups$getNavigation().setMaxVisitedNodesMultiplier(0.5F);
            ((MobAccessor)bee).bovinesandbuttercups$getNavigation().moveTo(vec32.x, vec32.y, vec32.z, 1.0D);
        }
    }

    private boolean shouldMoveToMoobloom() {
        return ((BeeAccessor)bee).bovinesandbuttercups$getTicksWithoutNectarSinceExitingHive() > 2400;
    }
}
