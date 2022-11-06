package net.merchantpug.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Shadow @Final private Player player;
    @Unique
    private String bovinesandbuttercups$capturedNameChange;
    @Unique
    private ItemStack bovinesandbuttercups$capturedStack;

    public AnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component component, ResourceLocation location) {
        super(menu, inventory, component, location);
    }

    @Inject(method = "onNameChanged", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private void bovinesandbuttercups$useNewNameChanged(String string, CallbackInfo ci) {
        bovinesandbuttercups$capturedNameChange = string;
    }

    @ModifyExpressionValue(method = "onNameChanged", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private boolean bovinesandbuttercups$useNewNameChanged(boolean original) {
        Slot slot = this.menu.getSlot(0);
        return original || slot.hasItem() && !slot.getItem().hasCustomHoverName() && bovinesandbuttercups$capturedNameChange.equals(slot.getItem().getHoverName().getString());
    }

    @Inject(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    private void bovinesandbuttercups$setDataDrivenItemName(AbstractContainerMenu menu, int i, ItemStack stack, CallbackInfo ci) {
        bovinesandbuttercups$capturedStack = stack;
    }

    @ModifyArg(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    private String bovinesandbuttercups$setDataDrivenItemName(String string) {
        if (BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack) != null && BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).contains("Type", Tag.TAG_STRING)) {
            if (bovinesandbuttercups$capturedStack.is(Services.PLATFORM.getCustomFlowerItem())) {
                return BovineRegistryUtil.getFlowerTypeFromKey(player.level, ResourceLocation.tryParse(BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).getString("Type"))).getOrCreateNameTranslationKey(player.level).getString();
            } else if (bovinesandbuttercups$capturedStack.is(Services.PLATFORM.getCustomMushroomItem())) {
                return BovineRegistryUtil.getMushroomTypeFromKey(player.level, ResourceLocation.tryParse(BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).getString("Type"))).getOrCreateNameTranslationKey(player.level).getString();
            } else if (bovinesandbuttercups$capturedStack.is(Services.PLATFORM.getCustomHugeMushroomItem())) {
                return BovineRegistryUtil.getMushroomTypeFromKey(player.level, ResourceLocation.tryParse(BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).getString("Type"))).getOrCreateHugeNameTranslationKey(player.level).getString();
            }
        }
        return string;
    }
}
