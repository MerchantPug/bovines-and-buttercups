package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackAccess {
    @Unique
    public Level bovinesandbuttercups$level;

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setPopTime(I)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$copyStackLevel(CallbackInfoReturnable<ItemStack> cir, ItemStack stack) {
        ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(this.bovinesandbuttercups$getLevel());
    }

    public Level bovinesandbuttercups$getLevel() {
        return bovinesandbuttercups$level;
    }

    public void bovinesandbuttercups$setLevel(Level level) {
        bovinesandbuttercups$level = level;
    }
}
