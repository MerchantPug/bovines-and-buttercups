package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.CraftingContainerAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftingContainer.class)
public class CraftingContainerMixin implements CraftingContainerAccess {
    @Unique private Level bovinesandbuttercups$level;

    @Override
    public Level bovinesandbuttercups$getLevel() {
        return bovinesandbuttercups$level;
    }

    @Override
    public void bovinesandbuttercups$setLevel(Level level) {
        this.bovinesandbuttercups$level = level;
    }
}
