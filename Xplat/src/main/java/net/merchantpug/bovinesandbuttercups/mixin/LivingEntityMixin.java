package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean canBeAffected(MobEffectInstance mobEffectInstance);

    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();

    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"))
    private void bovinesandbuttercups$addRandomLockdown(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if (effect.getEffect() instanceof LockdownEffect && ((MobEffectInstanceAccess)effect).bovinesandbuttercups$getLockedEffects().size() == 0) {
            Optional<Holder<MobEffect>> randomEffect = Registry.MOB_EFFECT.getRandom(this.level.random);
            while (randomEffect.isPresent() && (randomEffect.get().value() instanceof LockdownEffect || !this.canBeAffected(new MobEffectInstance(randomEffect.get().value())))) {
                randomEffect = Registry.MOB_EFFECT.getRandom(this.level.random);
            }
            randomEffect.ifPresent(statusEffectRegistryEntry -> {
                ((MobEffectInstanceAccess)effect).bovinesandbuttercups$addLockedEffect(statusEffectRegistryEntry.value(), effect.getDuration());
            });
        }
    }

    @Inject(method = "canBeAffected", at = @At(value = "RETURN"), cancellable = true)
    private void bovinesandbuttercups$cancelStatusEffectIfNullified(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
         this.getActiveEffects().forEach(instance -> {
             if (instance.getEffect() instanceof LockdownEffect && ((MobEffectInstanceAccess)instance).bovinesandbuttercups$getLockedEffects().containsKey(effect.getEffect())) {
                 cir.setReturnValue(false);
             }
         });
    }
}
