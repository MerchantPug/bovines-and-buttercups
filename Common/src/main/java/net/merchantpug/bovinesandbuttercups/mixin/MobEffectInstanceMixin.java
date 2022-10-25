package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {
    @Shadow public abstract MobEffect getEffect();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void bovinesandbuttercups$stopDurationTicks(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(BovineEffects.LOCKDOWN.get())) {
            if (this.getEffect() instanceof LockdownEffect) {
                HashMap<MobEffect, Integer> lockdownEffectsToUpdate = new HashMap<>();
                Services.COMPONENT.getLockdownMobEffects(entity).forEach(((statusEffect, integer) -> {
                    if (integer > 0) {
                        lockdownEffectsToUpdate.put(statusEffect, --integer);
                    }
                }));
                Services.COMPONENT.setLockdownMobEffects(entity, lockdownEffectsToUpdate);
                Services.COMPONENT.syncLockdownMobEffects(entity);
            }
            if (Services.COMPONENT.getLockdownMobEffects(entity).containsKey(this.getEffect())) {
                cir.setReturnValue(true);
            }
        }
    }
}