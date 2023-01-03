package net.merchantpug.bovinesandbuttercups.mixin;

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

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin {
    @Shadow public abstract MobEffect getEffect();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void bovinesandbuttercups$lockDuration(LivingEntity living, Runnable runnable, CallbackInfoReturnable<Boolean> cir) {
        if (living.hasEffect(BovineEffects.LOCKDOWN.get()) && Services.COMPONENT.getLockdownMobEffects(living).containsKey(this.getEffect()))
            cir.setReturnValue(true);
    }
}
