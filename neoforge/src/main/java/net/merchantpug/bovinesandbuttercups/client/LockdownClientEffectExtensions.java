package net.merchantpug.bovinesandbuttercups.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.List;
import java.util.Map;

public class LockdownClientEffectExtensions implements IClientMobEffectExtensions {
    @Override
    public boolean renderInventoryIcon(MobEffectInstance instance, EffectRenderingInventoryScreen<?> screen, GuiGraphics guiGraphics, int x, int y, int blitOffset) {
        Minecraft.getInstance().player.getCapability(LockdownEffectCapability.INSTANCE).map(cap -> cap.getLockdownMobEffects().entrySet().stream().toList()).ifPresent(list -> {
            if (!list.isEmpty()) {
                int lockdownEffectIndex = Minecraft.getInstance().player.tickCount / (160 / list.size()) % list.size();

                MobEffect mobEffect1 = list.get(lockdownEffectIndex).getKey();

                TextureAtlasSprite additionalSprite = Minecraft.getInstance().getMobEffectTextures().get(mobEffect1);
                RenderSystem.setShaderTexture(0, additionalSprite.atlasLocation());
                guiGraphics.blit(x, y + 7, blitOffset, 18, 18, additionalSprite);
            }
        });

        return false;
    }

    @Override
    public boolean renderGuiIcon(MobEffectInstance instance, Gui gui, GuiGraphics guiGraphics, int x, int y, float z, float alpha) {
        Minecraft.getInstance().player.getCapability(LockdownEffectCapability.INSTANCE).map(cap -> cap.getLockdownMobEffects().entrySet().stream().toList()).ifPresent(list -> {
            if (!list.isEmpty()) {
                int lockdownEffectIndex = Minecraft.getInstance().player.tickCount / (160 / list.size()) % list.size();

                MobEffect statusEffect1 = list.get(lockdownEffectIndex).getKey();

                List<Map.Entry<MobEffect, Integer>> runningOutEffectList = list.stream().filter(statusEffectIntegerEntry -> statusEffectIntegerEntry.getValue() <= 200 && statusEffectIntegerEntry.getValue() != -1).toList();

                float a = alpha;
                if (list.size() > 1 && !runningOutEffectList.isEmpty()) {
                    int runningOutEffectIndex = Minecraft.getInstance().player.tickCount / (160 / runningOutEffectList.size()) % runningOutEffectList.size();

                    if (!instance.isAmbient()) {
                        int duration = runningOutEffectList.get(runningOutEffectIndex).getValue();
                        int m = 10 - duration / 20;
                        a = Mth.clamp((float)duration / 10.0f / 5.0f * 0.5f, 0.0f, 0.5f) + Mth.cos((float)duration * (float)Math.PI / 5.0f) * Mth.clamp((float)m / 10.0f * 0.25f, 0.0f, 0.25f);
                    }

                    statusEffect1 = runningOutEffectList.get(runningOutEffectIndex).getKey();
                }

                TextureAtlasSprite additionalSprite = Minecraft.getInstance().getMobEffectTextures().get(statusEffect1);
                float finalAlpha =  a;

                RenderSystem.setShaderTexture(0, additionalSprite.atlasLocation());
                RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, finalAlpha);
                guiGraphics.blit(x + 3, y + 3, 0, 18, 18, additionalSprite);
            }
        });
        return false;
    }
}
