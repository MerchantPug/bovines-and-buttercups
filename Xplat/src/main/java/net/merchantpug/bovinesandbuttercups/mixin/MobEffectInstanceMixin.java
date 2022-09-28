package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(MobEffectInstance.class)
public abstract class MobEffectInstanceMixin implements MobEffectInstanceAccess {
    @Shadow public abstract MobEffect getEffect();

    private HashMap<MobEffect, Integer> bovinesandbuttercups$lockedEffects = new HashMap<>();

    @Inject(method = "setDetailsFrom", at = @At("TAIL"))
    private void bovinesandbuttercups$copyLockedEffects(MobEffectInstance that, CallbackInfo ci) {
        if (!(that.getEffect() instanceof LockdownEffect)) return;
        this.bovinesandbuttercups$lockedEffects = ((MobEffectInstanceAccess)that).bovinesandbuttercups$getLockedEffects();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void bovinesandbuttercups$stopDurationTicks(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        List<MobEffectInstance> effectList = entity.getActiveEffects().stream().filter(statusEffectInstance -> statusEffectInstance.getEffect() == BovineEffects.LOCKDOWN.get()).toList();
        if (effectList.size() > 0) {
            MobEffectInstance effectInstance = effectList.get(0);
            if (((MobEffectInstanceAccess)effectInstance).bovinesandbuttercups$getLockedEffects().containsKey(this.getEffect())) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "tickDownDuration", at = @At("RETURN"))
    private void bovinesandbuttercups$updateLockedEffectDuration(CallbackInfoReturnable<Integer> cir) {
        if (!(this.getEffect() instanceof LockdownEffect)) return;
        HashMap<MobEffect, Integer> nullifiedEffectsToUpdate = new HashMap<>();
        bovinesandbuttercups$lockedEffects.forEach(((statusEffect, integer) -> {
            if (integer > 0) {
                nullifiedEffectsToUpdate.put(statusEffect, --integer);
            }
        }));
        bovinesandbuttercups$lockedEffects = nullifiedEffectsToUpdate;
    }

    @Unique MobEffectInstance bovinesandbuttercups$capturedThatStatusEffect;

    @Inject(method = "update", at = @At("HEAD"))
    private void bovinesandbuttercups$captureThatStatusEffect(MobEffectInstance that, CallbackInfoReturnable<Boolean> cir) {
        this.bovinesandbuttercups$capturedThatStatusEffect = that;
    }

    @ModifyVariable(method = "update", at = @At(value = "STORE", ordinal = 0))
    private boolean bovinesandbuttercups$upgradeLockedEffects(boolean bl) {
        if (this.getEffect() instanceof LockdownEffect && bovinesandbuttercups$capturedThatStatusEffect.getEffect() instanceof LockdownEffect) {
            boolean hasAppliedAnEffect = false;
            for (Map.Entry<MobEffect, Integer> entry : ((MobEffectInstanceAccess)bovinesandbuttercups$capturedThatStatusEffect).bovinesandbuttercups$getLockedEffects().entrySet()) {
                if (!bovinesandbuttercups$lockedEffects.containsKey(entry.getKey()) || entry.getValue() > bovinesandbuttercups$lockedEffects.get(entry.getKey())) {
                    bovinesandbuttercups$lockedEffects.put(entry.getKey(), entry.getValue());
                    hasAppliedAnEffect = true;
                }
            }
            if (hasAppliedAnEffect) {
                return bl = true;
            }
        }
        return bl;
    }

    @Inject(method = "save", at = @At("RETURN"))
    private void bovinesandbuttercups$writeLockedEffectsAsNbt(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (!(this.getEffect() instanceof LockdownEffect)) return;
        ListTag list = new ListTag();
        bovinesandbuttercups$lockedEffects.forEach(((statusEffect, integer) -> {
            CompoundTag effectCompound = new CompoundTag();
            effectCompound.putByte("Id", (byte)MobEffect.getId(statusEffect));
            effectCompound.putInt("Duration", integer);
            list.add(effectCompound);
        }));
        tag.put("LockedEffects", list);
    }

    @Inject(method = "loadSpecifiedEffect", at = @At("RETURN"), cancellable = true)
    private static void bovinesandbuttercups$readLockedEffectsAsNbt(MobEffect type, CompoundTag tag, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (!(type instanceof LockdownEffect)) return;
        MobEffectInstance instance = cir.getReturnValue();
        if (!tag.contains("LockedEffects", Tag.TAG_LIST)) return;
        ListTag list = tag.getList("LockedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn("LockedEffects NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_BYTE) && compound.contains("Duration", Tag.TAG_INT)) {
                ((MobEffectInstanceAccess)instance).bovinesandbuttercups$addLockedEffect(MobEffect.byId(compound.getByte("Id")), compound.getInt("Duration"));
            }
        }
        cir.setReturnValue(instance);
    }

    @Override
    public HashMap<MobEffect, Integer> bovinesandbuttercups$getLockedEffects() {
        return bovinesandbuttercups$lockedEffects;
    }

    @Override
    public void bovinesandbuttercups$setLockedEffects(HashMap<MobEffect, Integer> lockedEffects) {
        this.bovinesandbuttercups$lockedEffects = lockedEffects;
    }

    @Override
    public void bovinesandbuttercups$addLockedEffect(MobEffect statusEffect, int duration) {
        this.bovinesandbuttercups$lockedEffects.put(statusEffect, duration);
    }

    @Override
    public void bovinesandbuttercups$removeLockedEffects(MobEffect statusEffect) {
        this.bovinesandbuttercups$lockedEffects.remove(statusEffect);
    }

    @Override
    public void bovinesandbuttercups$clearLockedEffects() {
        this.bovinesandbuttercups$lockedEffects.clear();
    }
}
