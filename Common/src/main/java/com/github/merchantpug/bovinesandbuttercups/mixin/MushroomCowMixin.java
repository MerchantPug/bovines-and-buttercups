package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MushroomCow.class)
public class MushroomCowMixin {
    @Inject(method = "mobInteract", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/animal/MushroomCow;effect:Lnet/minecraft/world/effect/MobEffect;" ,ordinal = 3), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void passIfExcludedFlower(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, ItemStack itemStack) {
        if (itemStack.is(BovineTags.SUSPICIOUS_STEW_RECIPE_EXCLUDED)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
