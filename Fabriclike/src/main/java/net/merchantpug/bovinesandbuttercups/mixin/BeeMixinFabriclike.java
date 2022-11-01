package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bee.class)
public class BeeMixinFabriclike {
    @Inject(method = "aiStep", at = @At("TAIL"))
    private void bovinesandbuttercupsmethod$handleAiStep(CallbackInfo ci) {
        if (((Entity)(Object)this).getLevel().isClientSide() || ((BeeAccess)(Object)this).bovinesandbuttercups$getPollinateFlowerCowGoal() == null) return;
        ((BeeAccess)(Object)this).bovinesandbuttercups$getPollinateFlowerCowGoal().tickCooldown();
    }
}
