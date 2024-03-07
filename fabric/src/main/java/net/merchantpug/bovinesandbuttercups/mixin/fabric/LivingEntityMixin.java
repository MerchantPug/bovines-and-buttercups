package net.merchantpug.bovinesandbuttercups.mixin.fabric;

import net.merchantpug.bovinesandbuttercups.attachment.api.LockdownEffectApi;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCriteriaTriggers;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityApis;
import net.minecraft.core.Holder;
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
        if (!this.level().isClientSide() && entity.hasEffect(BovineEffects.LOCKDOWN.get())) {
            HashMap<MobEffect, Integer> lockdownEffectsToUpdate = new HashMap<>();
            Services.COMPONENT.getLockdownMobEffects(entity).forEach(((statusEffect, integer) -> {
                if (integer > 0) {
                    lockdownEffectsToUpdate.put(statusEffect, --integer);
                } else if (integer == -1) {
                    lockdownEffectsToUpdate.put(statusEffect, -1);
                }
            }));
            Services.COMPONENT.setLockdownMobEffects(entity, lockdownEffectsToUpdate);
            Services.COMPONENT.syncLockdownMobEffects(entity);
        }
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void bovinesandbuttercups$addRandomLockdown(MobEffectInstance effect, Entity entity, CallbackInfo ci) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(this, null);
        if (!this.level().isClientSide && effect.getEffect() instanceof LockdownEffect && api != null && (api.getLockdownMobEffects().isEmpty() || api.getLockdownMobEffects().values().stream().allMatch(value -> value < effect.getDuration()))) {
            Optional<Holder.Reference<MobEffect>> randomEffect = BuiltInRegistries.MOB_EFFECT.getRandom(this.level().random);
            randomEffect.ifPresent(entry -> {
                api.addLockdownMobEffect(entry.value(), effect.getDuration());
                api.sync();
            });
        }
        if (!this.level().isClientSide && (LivingEntity)(Object)this instanceof ServerPlayer serverPlayer && effect.getEffect() instanceof LockdownEffect && api != null && api.getLockdownMobEffects().isEmpty()) {
            api.getLockdownMobEffects().forEach((effect1, duration) -> {
                if (!this.hasEffect(effect1)) return;
                BovineCriteriaTriggers.LOCK_EFFECT.get().trigger(serverPlayer, effect1);
            });
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void bovinesandbuttercups$updateWithRandomLockdown(MobEffectInstance effect, boolean bl, Entity entity, CallbackInfo ci) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(this, null);
        if (!this.level().isClientSide && effect.getEffect() instanceof LockdownEffect && api != null && (api.getLockdownMobEffects().isEmpty() || api.getLockdownMobEffects().values().stream().allMatch(value -> value < effect.getDuration()))) {
            Optional<Holder.Reference<MobEffect>> randomEffect = BuiltInRegistries.MOB_EFFECT.getRandom(this.level().random);
            randomEffect.ifPresent(entry -> {
                api.addLockdownMobEffect(entry.value(), effect.getDuration());
                api.sync();
            });
        }
    }

    @Inject(method = "onEffectRemoved", at = @At("TAIL"))
    private void bovinesandbuttercups$removeAllLockdownEffects(MobEffectInstance effect, CallbackInfo ci) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(this, null);
        if (this.level().isClientSide || !(effect.getEffect() instanceof LockdownEffect) || api == null) return;
        api.getLockdownMobEffects().clear();
        api.sync();
    }

    @Inject(method = "canBeAffected", at = @At(value = "RETURN"), cancellable = true)
    private void bovinesandbuttercups$cancelStatusEffectIfLocked(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(this, null);
        if (this.hasEffect(BovineEffects.LOCKDOWN.get()) && api != null && api.getLockdownMobEffects().containsKey(effect.getEffect())) {
            if (!this.level().isClientSide && (LivingEntity)(Object)this instanceof ServerPlayer serverPlayer) {
                BovineCriteriaTriggers.PREVENT_EFFECT.get().trigger(serverPlayer, effect.getEffect());
            }
            cir.setReturnValue(false);
        }
    }
}
