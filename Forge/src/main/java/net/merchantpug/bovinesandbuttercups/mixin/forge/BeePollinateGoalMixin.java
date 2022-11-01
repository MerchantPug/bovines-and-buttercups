package net.merchantpug.bovinesandbuttercups.mixin.forge;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.merchantpug.bovinesandbuttercups.access.BeeAccess;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeePollinateGoal")
public abstract class BeePollinateGoalMixin {
    @Final @Shadow Bee this$0;

    @Inject(method = "canBeeUse", at = @At(value = "HEAD"), cancellable = true)
    private void bovinesandbuttercups$addMoobloomToBee(CallbackInfoReturnable<Boolean> cir) {
        if (Services.COMPONENT.getMoobloomTarget(this$0).isEmpty()) return;
        cir.setReturnValue(false);
    }

    @ModifyReturnValue(method = "isPollinating", at = @At(value = "RETURN"))
    private boolean bovinesandbuttercups$isPollinatingMoobloomOrFlower(boolean value) {
        return value || ((BeeAccess)(Object)this$0).bovinesandbuttercups$getPollinateFlowerCowGoal().isPollinating();
    }
}
