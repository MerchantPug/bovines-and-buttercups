package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SuspiciousStewRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SuspiciousStewRecipe.class)
public class SuspiciousStewRecipeMixin {
    @Inject(method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void returnFalseIfMoobloomFlower(CraftingContainer $$0, Level $$1, CallbackInfoReturnable<Boolean> cir, boolean hasFlower, boolean hasRedMushroom, boolean hasBrownMushroom, boolean hasBowl, int i, ItemStack itemStack) {
        if (itemStack.is(BovineTags.SUSPICIOUS_STEW_RECIPE_EXCLUDED)) {
            cir.setReturnValue(false);
        }
    }
}
