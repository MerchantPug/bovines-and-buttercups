package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import com.github.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEffects;
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

    private HashMap<MobEffect, Integer> bovinesandbuttercups$nullifiedEffects = new HashMap<>();

    @Inject(method = "setDetailsFrom", at = @At("TAIL"))
    private void copyNullifiedEffects(MobEffectInstance that, CallbackInfo ci) {
        if (!(that.getEffect() instanceof LockdownEffect)) return;
        this.bovinesandbuttercups$nullifiedEffects = ((MobEffectInstanceAccess)that).bovinesandbuttercups$getNullifiedEffects();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;tickDownDuration()I"), cancellable = true)
    private void stopDurationTicks(LivingEntity entity, Runnable overwriteCallback, CallbackInfoReturnable<Boolean> cir) {
        List<MobEffectInstance> effectList = entity.getActiveEffects().stream().filter(statusEffectInstance -> statusEffectInstance.getEffect() == BovineEffects.LOCKDOWN.get()).toList();
        if (effectList.size() > 0) {
            MobEffectInstance effectInstance = effectList.get(0);
            if (((MobEffectInstanceAccess)effectInstance).bovinesandbuttercups$getNullifiedEffects().containsKey(this.getEffect())) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "tickDownDuration", at = @At("RETURN"))
    private void updateNullifiedEffectDuration(CallbackInfoReturnable<Integer> cir) {
        if (!(this.getEffect() instanceof LockdownEffect)) return;
        HashMap<MobEffect, Integer> nullifiedEffectsToUpdate = new HashMap<>();
        bovinesandbuttercups$nullifiedEffects.forEach(((statusEffect, integer) -> {
            if (integer > 0) {
                nullifiedEffectsToUpdate.put(statusEffect, --integer);
            }
        }));
        bovinesandbuttercups$nullifiedEffects = nullifiedEffectsToUpdate;
    }

    @Unique MobEffectInstance bovinesandbuttercups$capturedThatStatusEffect;

    @Inject(method = "update", at = @At("HEAD"))
    private void captureThatStatusEffect(MobEffectInstance that, CallbackInfoReturnable<Boolean> cir) {
        this.bovinesandbuttercups$capturedThatStatusEffect = that;
    }

    @ModifyVariable(method = "update", at = @At(value = "STORE", ordinal = 0))
    private boolean upgradeNullifiedEffects(boolean bl) {
        if (this.getEffect() instanceof LockdownEffect && bovinesandbuttercups$capturedThatStatusEffect.getEffect() instanceof LockdownEffect) {
            boolean hasAppliedAnEffect = false;
            for (Map.Entry<MobEffect, Integer> entry : ((MobEffectInstanceAccess)bovinesandbuttercups$capturedThatStatusEffect).bovinesandbuttercups$getNullifiedEffects().entrySet()) {
                if (!bovinesandbuttercups$nullifiedEffects.containsKey(entry.getKey()) || entry.getValue() > bovinesandbuttercups$nullifiedEffects.get(entry.getKey())) {
                    bovinesandbuttercups$nullifiedEffects.put(entry.getKey(), entry.getValue());
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
    private void writeNullifiedEffectsAsNbt(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        if (!(this.getEffect() instanceof LockdownEffect)) return;
        ListTag list = new ListTag();
        bovinesandbuttercups$nullifiedEffects.forEach(((statusEffect, integer) -> {
            CompoundTag effectCompound = new CompoundTag();
            effectCompound.putByte("Id", (byte)MobEffect.getId(statusEffect));
            effectCompound.putInt("Duration", integer);
            list.add(effectCompound);
        }));
        tag.put("NullifiedEffects", list);
    }

    @Inject(method = "loadSpecifiedEffect", at = @At("RETURN"), cancellable = true)
    private static void readNullifiedEffectsAsNbt(MobEffect type, CompoundTag tag, CallbackInfoReturnable<MobEffectInstance> cir) {
        if (!(type instanceof LockdownEffect)) return;
        MobEffectInstance instance = cir.getReturnValue();
        if (!tag.contains("NullifiedEffects", Tag.TAG_LIST)) return;
        ListTag list = tag.getList("NullifiedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                Constants.LOG.warn("NullifiedEffects NBT is not an NBTCompound.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_BYTE) && compound.contains("Duration", Tag.TAG_INT)) {
                ((MobEffectInstanceAccess)instance).bovinesandbuttercups$addNullifiedEffect(MobEffect.byId(compound.getByte("Id")), compound.getInt("Duration"));
            }
        }
        cir.setReturnValue(instance);
    }

    @Override
    public HashMap<MobEffect, Integer> bovinesandbuttercups$getNullifiedEffects() {
        return bovinesandbuttercups$nullifiedEffects;
    }

    @Override
    public void bovinesandbuttercups$setNullifiedEffects(HashMap<MobEffect, Integer> nullifiedEffects) {
        this.bovinesandbuttercups$nullifiedEffects = nullifiedEffects;
    }

    @Override
    public void bovinesandbuttercups$addNullifiedEffect(MobEffect statusEffect, int duration) {
        this.bovinesandbuttercups$nullifiedEffects.put(statusEffect, duration);
    }

    @Override
    public void bovinesandbuttercups$removeNullifiedEffects(MobEffect statusEffect) {
        this.bovinesandbuttercups$nullifiedEffects.remove(statusEffect);
    }

    @Override
    public void bovinesandbuttercups$clearNullifiedEffects() {
        this.bovinesandbuttercups$nullifiedEffects.clear();
    }
}
