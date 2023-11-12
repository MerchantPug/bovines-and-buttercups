package net.merchantpug.bovinesandbuttercups.mixin.fabric.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
import java.util.Map;

@Mixin(Gui.class)
public class GuiMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(GuiGraphics graphics, CallbackInfo ci, Collection collection, int k, int l, MobEffectTextureManager mobEffectTextureManager, List<Runnable> runnables, Iterator iterator, MobEffectInstance mobEffectInstance, MobEffect effect, int width, int height) {
        if (minecraft.player == null || !minecraft.player.hasEffect(BovineEffects.LOCKDOWN.get())) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(minecraft.player).getLockdownMobEffects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            graphics.blitSprite(BovinesAndButtercups.asResource("hud/lockdown_frame"), width, height, 24, 24);
        }
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$renderLockdownStatusEffectOverlay(GuiGraphics guiGraphics, CallbackInfo ci, Collection collection, int i, int j, MobEffectTextureManager mobEffectTextureManager, List<Runnable> list, Iterator var7, MobEffectInstance mobEffectInstance, MobEffect mobEffect, int k, int l, float f, TextureAtlasSprite textureAtlasSprite, int n, int o, float g) {
        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect)) return;

        List<Map.Entry<MobEffect, Integer>> statusEffectList = BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(minecraft.player).getLockdownMobEffects().entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        MobEffect statusEffect1 = statusEffectList.get(lockdownEffectIndex).getKey();

        List<Map.Entry<MobEffect, Integer>> runningOutEffectList = statusEffectList.stream().filter(statusEffectIntegerEntry -> statusEffectIntegerEntry.getValue() <= 200).toList();

        float alpha = g;
        if (statusEffectList.size() > 1) {
            if (!runningOutEffectList.isEmpty()) {
                int runningOutEffectIndex = minecraft.player.tickCount / (160 / runningOutEffectList.size()) % runningOutEffectList.size();

                if (!mobEffectInstance.isAmbient()) {
                    int duration = runningOutEffectList.get(runningOutEffectIndex).getValue();
                    int m = 10 - duration / 20;
                    alpha = Mth.clamp((float)duration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)duration * (float)Math.PI / 5.0f) * Mth.clamp((float)m / 10.0f * 0.25f, 0.0f, 0.25f);
                }
                statusEffect1 = runningOutEffectList.get(runningOutEffectIndex).getKey();
            }
        }

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(statusEffect1);
        float a = alpha;

        list.add(() -> {
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, a);
            guiGraphics.blit(n + 3, o + 3, 0, 18, 18, additionalSprite);
            guiGraphics.setColor(1.0f, 1.0f, 1.0f, 1.0F);
        });
    }
}
