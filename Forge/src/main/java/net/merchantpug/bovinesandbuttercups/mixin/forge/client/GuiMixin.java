package net.merchantpug.bovinesandbuttercups.mixin.forge.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
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
public class GuiMixin {
    @Shadow @Final protected Minecraft minecraft;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(GuiGraphics guiGraphics, CallbackInfo ci, Collection<MobEffectInstance> collection, Screen screen, int i, int j, MobEffectTextureManager mobEffectTextureManager, List<Runnable> runnables, Iterator it, MobEffectInstance mobEffectInstance, MobEffect effect, IClientMobEffectExtensions extensions, int k, int l) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovineEffects.LOCKDOWN.get())) return;

        minecraft.player.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> {
            if (!(effect instanceof LockdownEffect) && cap.getLockdownMobEffects().entrySet().stream().anyMatch(instance -> instance.getKey() == effect)) {
                guiGraphics.blit(BovinesAndButtercups.asResource("textures/gui/container/lockdown_frame.png"), k, l, 36, 4, 24, 24, 64, 32);
            }
        });
    }
}