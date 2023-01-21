package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {
    @Unique
    private ItemStack bovinesandbuttercups$capturedStack;

    public AnvilScreenMixin(AnvilMenu menu, Inventory inventory, Component component, ResourceLocation location) {
        super(menu, inventory, component, location);
    }

    @Inject(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    private void bovinesandbuttercups$setDataDrivenItemName(AbstractContainerMenu menu, int i, ItemStack stack, CallbackInfo ci) {
        bovinesandbuttercups$capturedStack = stack;
    }

    /*
    @ModifyArg(method = "slotChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/EditBox;setValue(Ljava/lang/String;)V"))
    private String bovinesandbuttercups$setDataDrivenItemName(String string) {
        if (BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack) != null && BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).contains("Type", Tag.TAG_STRING)) {
            if (bovinesandbuttercups$capturedStack.is(BovineItems.CUSTOM_FLOWER.get()) || bovinesandbuttercups$capturedStack.is(BovineItems.CUSTOM_MUSHROOM.get())) {
                ResourceLocation resourceLocation = ResourceLocation.tryParse(BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).getString("Type"));
                return Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath()).getString();
            } else if (bovinesandbuttercups$capturedStack.is(BovineItems.CUSTOM_MUSHROOM_BLOCK.get())) {
                ResourceLocation resourceLocation = ResourceLocation.tryParse(BlockItem.getBlockEntityData(bovinesandbuttercups$capturedStack).getString("Type"));
                return Component.translatable("block." + resourceLocation.getNamespace() + "." + resourceLocation.getPath() + "_block").getString();
            }
        }
        return string;
    }
    */
}
