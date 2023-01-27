package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCriteriaTriggers;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean hasEffect(MobEffect mobEffect);

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tickEffects()V", shift = At.Shift.AFTER))
    private void bovinesandbuttercups$tickLockdown(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.hasEffect(BovineEffects.LOCKDOWN.get())) {
            HashMap<MobEffect, Integer> lockdownEffectsToUpdate = new HashMap<>();
            Services.COMPONENT.getLockdownMobEffects(entity).forEach(((statusEffect, integer) -> {
                if (integer > 0) {
                    lockdownEffectsToUpdate.put(statusEffect, --integer);
                }
            }));
            Services.COMPONENT.setLockdownMobEffects(entity, lockdownEffectsToUpdate);
        }
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void bovinesandbuttercups$addRandomLockdown(MobEffectInstance effect, Entity entity, CallbackInfo ci) {
        if (!this.level.isClientSide && effect.getEffect() instanceof LockdownEffect && (BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().isEmpty() || BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().values().stream().allMatch(value -> value < effect.getDuration()))) {
            Optional<Holder.Reference<MobEffect>> randomEffect = BuiltInRegistries.MOB_EFFECT.getRandom(this.level.random);
            randomEffect.ifPresent(entry -> {
                BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).addLockdownMobEffect(entry.value(), effect.getDuration());
                BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.sync(this);
            });
        }
        if (!this.level.isClientSide && (LivingEntity)(Object)this instanceof ServerPlayer serverPlayer && effect.getEffect() instanceof LockdownEffect && !BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().isEmpty()) {
            BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().forEach((effect1, duration) -> {
                if (!this.hasEffect(effect1)) return;
                BovineCriteriaTriggers.LOCK_EFFECT.trigger(serverPlayer, effect1);
            });
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void bovinesandbuttercups$updateWithRandomLockdown(MobEffectInstance effect, boolean bl, Entity entity, CallbackInfo ci) {
        if (!this.level.isClientSide && effect.getEffect() instanceof LockdownEffect && (BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().isEmpty() || BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().values().stream().allMatch(value -> value < effect.getDuration()))) {
            Optional<Holder.Reference<MobEffect>> randomEffect = BuiltInRegistries.MOB_EFFECT.getRandom(this.level.random);
            randomEffect.ifPresent(entry -> {
                BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).addLockdownMobEffect(entry.value(), effect.getDuration());
                BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.sync(this);
            });
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void bovinesandbuttercups$removeAllLockdownEffects(MobEffectInstance effect, CallbackInfo ci) {
        if (this.level.isClientSide || !(effect.getEffect() instanceof LockdownEffect)) return;
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().clear();
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.sync(this);
    }

    @Inject(method = "canBeAffected", at = @At(value = "RETURN"), cancellable = true)
    private void bovinesandbuttercups$cancelStatusEffectIfLocked(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(BovineEffects.LOCKDOWN.get()) && BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(this).getLockdownMobEffects().containsKey(effect.getEffect())) {
            if (!this.level.isClientSide && (LivingEntity)(Object)this instanceof ServerPlayer serverPlayer) {
                BovineCriteriaTriggers.PREVENT_EFFECT.trigger(serverPlayer, effect.getEffect());
            }
            cir.setReturnValue(false);
        }
    }
}
