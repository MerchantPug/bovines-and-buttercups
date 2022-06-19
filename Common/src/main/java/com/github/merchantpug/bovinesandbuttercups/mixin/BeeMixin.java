package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.access.BeeAccess;
import com.github.merchantpug.bovinesandbuttercups.entity.goal.MoveToMoobloomGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Bee.class)
public abstract class BeeMixin extends Animal implements BeeAccess {
    @Nullable @Unique private UUID moobloomUUID;

    protected BeeMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee;hasSavedFlowerPos()Z"))
    private void writeCustomDataToNbt(CompoundTag tag, CallbackInfo ci) {
        if (this.moobloomUUID != null) {
            tag.putUUID("MoobloomTarget", moobloomUUID);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/Bee;savedFlowerPos:Lnet/minecraft/core/BlockPos;", ordinal = 0))
    private void readMoobloomFromNbt(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("MoobloomTarget")) {
            moobloomUUID = tag.getUUID("MoobloomTarget");
        }
    }

    @Inject(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee$BeeGrowCropGoal;<init>(Lnet/minecraft/world/entity/animal/Bee;)V"))
    private void addMoveToMoobloomGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(6, new MoveToMoobloomGoal((Bee) (Object)this));
    }

    @Nullable public UUID bovinesandbuttercups$getTargetMoobloom() {
        return moobloomUUID;
    }

    public void bovinesandbuttercups$setTargetMoobloom(@Nullable UUID value) {
        moobloomUUID = value;
    }
}