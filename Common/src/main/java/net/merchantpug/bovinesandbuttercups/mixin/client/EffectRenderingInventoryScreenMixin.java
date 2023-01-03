package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstances, boolean wide, CallbackInfo ci, int i, Iterator var7, MobEffectInstance mobEffectInstance) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = this.minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect() instanceof LockdownEffect).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && Services.COMPONENT.getLockdownMobEffects(this.minecraft.player).entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect()) && mobEffectInstance.getAmplifier() < 2) {
            RenderSystem.setShaderTexture(0, BovinesAndButtercups.asResource("textures/gui/container/lockdown_frame.png"));
            blit(poseStack, x, i,0, 0, 0, 32, 32, 64, 32);
            RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
        }
    }
}
