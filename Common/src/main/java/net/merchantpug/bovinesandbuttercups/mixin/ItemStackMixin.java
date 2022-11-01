package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackAccess {
    public Level bovinesandbuttercups$level;

    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void bovinesandbuttercups$addMoobloomRelatedGoals(Level level, Entity entity, int i, boolean bl, CallbackInfo ci) {
        if (this.bovinesandbuttercups$getLevel() == null) {
            this.bovinesandbuttercups$setLevel(level);
        }
    }

    public Level bovinesandbuttercups$getLevel() {
        return bovinesandbuttercups$level;
    }

    public void bovinesandbuttercups$setLevel(Level level) {
        bovinesandbuttercups$level = level;
    }
}
