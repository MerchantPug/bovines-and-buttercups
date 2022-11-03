package net.merchantpug.bovinesandbuttercups.mixin;

import net.merchantpug.bovinesandbuttercups.access.CraftingContainerAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {
    @Shadow @Final private CraftingContainer craftSlots;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$addLevelToContainer(Inventory inventory, boolean active, Player player, CallbackInfo ci) {
        ((CraftingContainerAccess)this.craftSlots).bovinesandbuttercups$setLevel(player.level);
    }
}
