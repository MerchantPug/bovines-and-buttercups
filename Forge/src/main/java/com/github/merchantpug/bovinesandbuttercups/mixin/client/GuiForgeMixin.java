package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import com.github.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.EffectRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(Gui.class)
public class GuiForgeMixin extends GuiComponent {
    @Shadow @Final private Minecraft minecraft;
    @Unique private int nullifiedEffectIndex;
    @Unique private int nullifiedEffectRunningOutIndex;
    @Unique private int nullifiedEffectTicks;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void overlayLockdownBorder(PoseStack poseStack, CallbackInfo ci, Collection<MobEffectInstance> collection, Screen screen, int i, int j, MobEffectTextureManager mobEffectTextureManager, List list, Iterator var7, MobEffectInstance mobEffectInstance, MobEffect mobEffect, EffectRenderer effectRenderer, int k, int l, float f) {
        List<MobEffectInstance> lockdownEffectInstance = collection.stream().filter(instance -> instance.getEffect() instanceof LockdownEffect).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && ((MobEffectInstanceAccess)lockdownEffectInstance.get(0)).bovinesandbuttercups$getNullifiedEffects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            RenderSystem.setShaderTexture(0, BovinesAndButtercupsCommon.resourceLocation("textures/gui/container/lockdown_frame.png"));
            blit(poseStack, k, l, this.getBlitOffset(), 36, 4, 24, 24, 64, 32);
            RenderSystem.setShaderTexture(0, AbstractContainerScreen.INVENTORY_LOCATION);
        }
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void renderLockdownStatusEffectOverlay(PoseStack poseStack, CallbackInfo ci, Collection collection, Screen screen, int i, int j, MobEffectTextureManager mobEffectTextureManager, List<Runnable> list, Iterator var8, MobEffectInstance mobEffectInstance, MobEffect mobEffect, EffectRenderer renderer, int k, int l, float f, TextureAtlasSprite textureAtlasSprite, int n, int o, float g) {
        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect)) return;

        List<Map.Entry<MobEffect, Integer>> statusEffectList = ((MobEffectInstanceAccess)mobEffectInstance).bovinesandbuttercups$getNullifiedEffects().entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        if (nullifiedEffectTicks % Math.max(600, 1200 - ((statusEffectList.size() - 2) * 300)) == 0) {
            nullifiedEffectIndex = nullifiedEffectIndex < statusEffectList.size() - 1 ? nullifiedEffectIndex + 1 : 0;
        }

        MobEffect statusEffect1 = nullifiedEffectIndex > statusEffectList.size() - 1 ? statusEffectList.get(0).getKey() : statusEffectList.get(nullifiedEffectIndex).getKey();

        List<Map.Entry<MobEffect, Integer>> runningOutEffectList = statusEffectList.stream().filter(statusEffectIntegerEntry -> statusEffectIntegerEntry.getValue() <= 200).toList();

        float alpha = g;
        if (statusEffectList.size() > 1) {
            if (!runningOutEffectList.isEmpty()) {
                if (nullifiedEffectTicks % Math.max(600, 1200 - ((runningOutEffectList.size() - 2) * 300)) == 0) {
                    nullifiedEffectRunningOutIndex = nullifiedEffectRunningOutIndex < runningOutEffectList.size() - 1 ? nullifiedEffectRunningOutIndex + 1 : 0;
                }
                if (!mobEffectInstance.isAmbient()) {
                    int duration = nullifiedEffectRunningOutIndex > runningOutEffectList.size() - 1 ? runningOutEffectList.get(0).getValue() : runningOutEffectList.get(nullifiedEffectRunningOutIndex).getValue();
                    int m = 10 - duration / 20;
                    alpha = Mth.clamp((float)duration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)duration * (float)Math.PI / 5.0f) * Mth.clamp((float)m / 10.0f * 0.25f, 0.0f, 0.25f);
                }
                statusEffect1 = nullifiedEffectRunningOutIndex > runningOutEffectList.size() - 1 ? runningOutEffectList.get(0).getKey() :  runningOutEffectList.get(nullifiedEffectRunningOutIndex).getKey();
            }
        }

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(statusEffect1);
        float a = alpha;

        list.add(() -> {
            RenderSystem.setShaderTexture(0, additionalSprite.atlas().getId());
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, a);
            blit(poseStack, n + 3, o + 3, this.getBlitOffset(), 18, 18, additionalSprite);
        });
        if (this.minecraft != null && this.minecraft.isPaused()) return;
        nullifiedEffectTicks++;
    }
}
