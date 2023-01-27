package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.content.item.CustomFlowerItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.item.crafting.SuspiciousStewRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(SuspiciousStewRecipe.class)
public class SuspiciousStewRecipeMixin {
    @Inject(method = "assemble(Lnet/minecraft/world/inventory/CraftingContainer;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$saveCustomFlowerMobEffect(CraftingContainer container, CallbackInfoReturnable<ItemStack> cir, ItemStack stewStack, int i, ItemStack flowerStack) {
        if (flowerStack.getItem() instanceof CustomFlowerItem) {
            MobEffect effect = CustomFlowerItem.getSuspiciousStewEffect(flowerStack);
            int duration = CustomFlowerItem.getSuspiciousStewDuration(flowerStack);
            SuspiciousStewItem.saveMobEffect(stewStack, effect, duration);
        }
    }
}
