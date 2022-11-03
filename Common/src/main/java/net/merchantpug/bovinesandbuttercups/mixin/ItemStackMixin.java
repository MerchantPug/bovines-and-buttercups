package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.util.ItemLevelUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ItemStackAccess {
    @Unique
    public Level bovinesandbuttercups$level;

    public Level bovinesandbuttercups$getLevel() {
        return bovinesandbuttercups$level;
    }

    public void bovinesandbuttercups$setLevel(Level level) {
        bovinesandbuttercups$level = level;
    }
}
