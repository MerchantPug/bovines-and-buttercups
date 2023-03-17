package net.merchantpug.bovinesandbuttercups.mixin.forge.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Mixin(Gui.class)
public class GuiMixin extends GuiComponent {
    @Shadow @Final
    protected Minecraft minecraft;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(PoseStack poseStack, CallbackInfo ci, Collection<MobEffectInstance> collection, Screen screen, int i, int j, MobEffectTextureManager mobEffectTextureManager, List list, Iterator var7, MobEffectInstance mobEffectInstance, MobEffect mobEffect, IClientMobEffectExtensions effectExtensions, int k, int l, float f) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovineEffects.LOCKDOWN.get())) return;

        minecraft.player.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
           if (cap.getLockdownMobEffects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
                RenderSystem.setShaderTexture(0, BovinesAndButtercups.asResource("textures/gui/container/lockdown_frame.png"));
                blit(poseStack, k, l, 0, 36, 4, 24, 24, 64, 32);
                RenderSystem.setShaderTexture(0, AbstractContainerScreen.INVENTORY_LOCATION);
            }
        });
    }
}