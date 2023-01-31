package net.merchantpug.bovinesandbuttercups.mixin.fabric;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.MoveToFlowerCowGoal;
import net.merchantpug.bovinesandbuttercups.content.entity.goal.PollinateFlowerCowGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public abstract class BeeMixin {

    @Shadow public abstract GoalSelector getGoalSelector();

    @Inject(method = "registerGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/Bee$BeePollinateGoal;<init>(Lnet/minecraft/world/entity/animal/Bee;)V", shift = At.Shift.BEFORE))
    private void bovinesandbuttercups$addMoobloomRelatedGoals(CallbackInfo ci) {
        PollinateFlowerCowGoal pollinateGoal = new PollinateFlowerCowGoal((Bee)(Object)this);
        this.getGoalSelector().addGoal(4, pollinateGoal);
        this.getGoalSelector().addGoal(6, new MoveToFlowerCowGoal((Bee)(Object)this));
        ((BeeAccess) this).bovinesandbuttercups$setPollinateFlowerCowGoal(pollinateGoal);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void bovinesandbuttercupsmethod$handleAiStep(CallbackInfo ci) {
        if (((Entity)(Object)this).getLevel().isClientSide() || ((BeeAccess) this).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) return;
        ((BeeAccess) this).bovinesandbuttercups$getPollinateFlowerCowGoal().tickCooldown();
    }
}
