package net.merchantpug.bovinesandbuttercups.mixin.neoforge;

import net.merchantpug.bovinesandbuttercups.attachment.capability.FlowerCowTargetCapability;
import net.merchantpug.bovinesandbuttercups.attachment.capability.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.attachment.capability.MushroomCowTypeCapability;
import net.merchantpug.bovinesandbuttercups.registry.BovineCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.capabilities.EntityCapability;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract <T> T getCapability(EntityCapability<T, Void> par1);

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$loadFromLegacyCaps(CompoundTag tag, CallbackInfo ci) {
        LockdownEffectCapability lockdownCap = this.getCapability(BovineCapabilities.LOCKDOWN_EFFECT);
        if (lockdownCap != null) {
            lockdownCap.deserializeLegacyCap(tag);
        }
        MushroomCowTypeCapability mooshroomCap = this.getCapability(BovineCapabilities.MOOSHROOM_TYPE);
        if (mooshroomCap != null) {
            mooshroomCap.deserializeLegacyCap(tag);
        }
        FlowerCowTargetCapability targetCap = this.getCapability(BovineCapabilities.MOOBLOOM_TARGET);
        if (targetCap != null) {
            targetCap.deserializeLegacyCap(tag);
        }
    }
}
