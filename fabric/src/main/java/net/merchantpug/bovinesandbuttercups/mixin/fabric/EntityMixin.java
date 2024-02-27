package net.merchantpug.bovinesandbuttercups.mixin.fabric;

import net.merchantpug.bovinesandbuttercups.attachment.api.FlowerCowTargetApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.LockdownEffectApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.MushroomCowTypeApi;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityApis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$loadFromLegacyCaps(CompoundTag tag, CallbackInfo ci) {
        LockdownEffectApi lockdownApi = BovineEntityApis.LOCKDOWN_EFFECTS.find((Entity)(Object)this, null);
        if (lockdownApi != null) {
            lockdownApi.deserializeLegacyCap(tag);
        }
        MushroomCowTypeApi mooshroomApi = BovineEntityApis.MOOSHROOM_TYPE.find((Entity)(Object)this, null);
        if (mooshroomApi != null) {
            mooshroomApi.deserializeLegacyCap(tag);
        }
        FlowerCowTargetApi targetApi = BovineEntityApis.MOOBLOOM_TARGET.find((Entity)(Object)this, null);
        if (targetApi != null) {
            targetApi.deserializeLegacyCap(tag);
        }
    }
}
