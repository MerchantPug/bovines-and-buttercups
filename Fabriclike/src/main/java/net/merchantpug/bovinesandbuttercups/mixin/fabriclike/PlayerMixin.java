package net.merchantpug.bovinesandbuttercups.mixin.fabriclike;

import net.merchantpug.bovinesandbuttercups.access.ItemStackAccess;
import net.merchantpug.bovinesandbuttercups.util.ItemLevelUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    @Shadow public AbstractContainerMenu containerMenu;

    @Shadow @Final public InventoryMenu inventoryMenu;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;tick()V", shift = At.Shift.AFTER))
    private void bovinesandbuttercups$setTick(CallbackInfo ci) {
        for (int i = 0; i < this.containerMenu.slots.size(); ++i) {
            ItemStack stack = this.containerMenu.slots.get(i).getItem();
            if (!ItemLevelUtil.isApplicableForStoringLevel(stack) || ((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel() != null) continue;
            ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(this.getLevel());
        }
        for (int i = 0; i < this.inventoryMenu.slots.size(); ++i) {
            ItemStack stack = this.inventoryMenu.getSlot(i).getItem();
            if (!ItemLevelUtil.isApplicableForStoringLevel(stack) || ((ItemStackAccess)(Object)stack).bovinesandbuttercups$getLevel() != null) continue;
            ((ItemStackAccess)(Object)stack).bovinesandbuttercups$setLevel(this.getLevel());
        }
    }
}
