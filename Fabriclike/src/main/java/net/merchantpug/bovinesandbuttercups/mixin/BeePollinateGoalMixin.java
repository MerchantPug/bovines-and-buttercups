package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("ConstantConditions")
@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public abstract class BeePollinateGoalMixin {
    @Final @Shadow Bee field_20377;

    @Inject(method = "canBeeUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee$BeePollinateGoal;findNearbyFlower()Ljava/util/Optional;"), cancellable = true)
    private void bovinesandbuttercups$addMoobloomToBee(CallbackInfoReturnable<Boolean> cir) {
        if (bovinesandbuttercups$findMoobloom().isPresent()) {
            ((BeeAccess)this.field_20377).bovinesandbuttercups$setTargetMoobloom(bovinesandbuttercups$findMoobloom().get().getUUID());
            Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
            if (!(entity instanceof FlowerCow moobloom)) {
                return;
            }
            moobloom.setStandingStillForBeeTicks(600);
            moobloom.setBee(field_20377);
            field_20377.setSavedFlowerPos(moobloom.blockPosition());
            ((MobAccessor)this.field_20377).bovinesandbuttercups$getNavigation().moveTo(moobloom.position().x(), moobloom.getBoundingBox().getYsize() * 1.3, moobloom.position().z(), 1.6f);
            cir.setReturnValue(true);
        }
    }

    @Unique
    private Optional<FlowerCow> bovinesandbuttercups$findMoobloom() {
        FlowerCow moobloom = this.field_20377.level.getNearestEntity(FlowerCow.class, TargetingConditions.forNonCombat().selector(entity -> entity.getLastHurtByMobTimestamp() <= entity.tickCount - 100 && entity.level.getBlockState(entity.blockPosition().above(2)).isAir() && !entity.isBaby() && ((FlowerCow)entity).bee == null), null, field_20377.getX(), field_20377.getY(), field_20377.getZ(), field_20377.getBoundingBox().inflate(8.0F, 4.0, 8.0F));
        if (moobloom != null) {
            return Optional.of(moobloom);
        }
        return Optional.empty();
    }

    @Inject(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;setHasNectar(Z)V"))
    private void bovinesandbuttercups$pollinateMoobloom(CallbackInfo ci) {
        Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) return;
        if (moobloom != null) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            moobloom.setPollinationTicks(2400);
            ((BeeAccess) field_20377).bovinesandbuttercups$setTargetMoobloom(null);
            field_20377.setSavedFlowerPos(null);
        }
    }

    @Inject(method = "canBeeContinueToUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;isFlowerValid(Lnet/minecraft/core/BlockPos;)Z"), cancellable = true)
    private void bovinesandbuttercups$removeIfMoobloomRemoved(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) return;
        if (this.field_20377.tickCount % 20 == 0 && moobloom != null && (!moobloom.isAlive() || moobloom.getLastHurtByMobTimestamp() > moobloom.tickCount - 100)) {
            moobloom.setStandingStillForBeeTicks(0);
            moobloom.setBee(null);
            ((BeeAccess) field_20377).bovinesandbuttercups$setTargetMoobloom(null);
            field_20377.setSavedFlowerPos(null);
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/phys/Vec3;add(DDD)Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 bovinesandbuttercups$addMoobloomTargeting(Vec3 value) {
        Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) return value;
        if (moobloom != null) {
            moobloom.setStandingStillForBeeTicks(600);
            moobloom.setBee(this.field_20377);
            return moobloom.position().add(0.0f, moobloom.getBoundingBox().getYsize() * 1.3, 0.0f);
        }
        return value;
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;savedFlowerPos:Lnet/minecraft/core/BlockPos;", ordinal = 0))
    private void bovinesandbuttercups$removeMoobloomIfTakingTooLong(CallbackInfo ci) {
        Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) return;
        moobloom.setStandingStillForBeeTicks(0);
        moobloom.setBee(null);
        field_20377.setSavedFlowerPos(null);
        ((BeeAccess) field_20377).bovinesandbuttercups$setTargetMoobloom(null);
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;savedFlowerPos:Lnet/minecraft/core/BlockPos;", ordinal = 2))
    private void bovinesandbuttercups$removeMoobloomIfTakingTooLongTwo(CallbackInfo ci) {
        Entity entity = ((ServerLevel)field_20377.level).getEntity(((BeeAccess)field_20377).bovinesandbuttercups$getTargetMoobloom());
        if (!(entity instanceof FlowerCow moobloom)) return;
        moobloom.setStandingStillForBeeTicks(0);
        moobloom.setBee(null);
        field_20377.setSavedFlowerPos(null);
        ((BeeAccess) field_20377).bovinesandbuttercups$setTargetMoobloom(null);
    }
}
