package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(GuiGraphics guiGraphics, int x, int height, Iterable<MobEffectInstance> iterable, boolean wide, CallbackInfo ci, int i, Iterator var7, MobEffectInstance mobEffectInstance) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = this.minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect() instanceof LockdownEffect).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && Services.COMPONENT.getLockdownMobEffects(this.minecraft.player).entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            guiGraphics.blitSprite(BovinesAndButtercups.asResource("container/inventory/lockdown_frame"), x, i, 32, 32);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I", ordinal = 1))
    private void bovinesandbuttercups$drawEffectDescriptionWhenHoveredOver(GuiGraphics guiGraphics, int i, int j, Iterable<MobEffectInstance> iterable, CallbackInfo ci) {
        if (this.minecraft == null) return;
        int mouseX = (int)(this.minecraft.mouseHandler.xpos() * (double)this.minecraft.getWindow().getGuiScaledWidth() / (double)this.minecraft.getWindow().getScreenWidth());
        int mouseY = (int)(this.minecraft.mouseHandler.ypos() * (double)this.minecraft.getWindow().getGuiScaledHeight() / (double)this.minecraft.getWindow().getScreenHeight());
        if (mouseX >= i && mouseX <= i + 119) {
            int l = this.topPos;
            MobEffectInstance mobEffectInstance = null;
            for (MobEffectInstance mobEffectInstance2 : iterable) {
                if (mouseY >= l && mouseY <= l + j && mobEffectInstance2.getEffect() instanceof LockdownEffect) {
                    mobEffectInstance = mobEffectInstance2;
                }
                l += j;
            }
            if (mobEffectInstance != null) {
                List<Component> list = List.of(this.getEffectName(mobEffectInstance), MobEffectUtil.formatDuration(mobEffectInstance, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate()));
                guiGraphics.renderTooltip(this.font, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

}
