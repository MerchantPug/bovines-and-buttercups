package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import com.github.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import com.github.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    @Unique private int bovinesandbuttercups$nullifiedEffectIndex;
    @Unique private int bovinesandbuttercups$nullifiedEffectTicks;

    public EffectRenderingInventoryScreenMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void overlayLockdownBorder(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstances, boolean wide, CallbackInfo ci, int i, Iterator var7, MobEffectInstance mobEffectInstance) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = this.minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect() instanceof LockdownEffect).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && ((MobEffectInstanceAccess)lockdownEffectInstance.get(0)).bovinesandbuttercups$getNullifiedEffects().entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            RenderSystem.setShaderTexture(0, BovinesAndButtercupsCommon.resourceLocation("textures/gui/container/lockdown_frame.png"));
            blit(poseStack, x, i,0, 0, 0, 32, 32, 64, 32);
            RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
        }
    }

    @Inject(method = "renderIcons", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void drawOverridenEffectSprite(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstanceIterable, boolean large, CallbackInfo ci, MobEffectTextureManager mobEffectTextureManager, int i, Iterator iterator, MobEffectInstance mobEffectInstance, MobEffect mobEffect, TextureAtlasSprite sprite) {
        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect)) return;

        List<Map.Entry<MobEffect, Integer>> statusEffectList = ((MobEffectInstanceAccess)mobEffectInstance).bovinesandbuttercups$getNullifiedEffects().entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        if (bovinesandbuttercups$nullifiedEffectTicks % Math.max(600, 1200 - ((statusEffectList.size() - 2) * 300)) == 0) {
            bovinesandbuttercups$nullifiedEffectIndex = bovinesandbuttercups$nullifiedEffectIndex < statusEffectList.size() - 1 ? bovinesandbuttercups$nullifiedEffectIndex + 1 : 0;
        }

        MobEffect mobEffect1 = bovinesandbuttercups$nullifiedEffectIndex > statusEffectList.size() - 1 ? statusEffectList.get(0).getKey() : statusEffectList.get(bovinesandbuttercups$nullifiedEffectIndex).getKey();

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(mobEffect1);
        RenderSystem.setShaderTexture(0, additionalSprite.atlas().getId());
        InventoryScreen.blit(poseStack, x + (large ? 6 : 7), i + 7, this.getBlitOffset(), 18, 18, additionalSprite);
        bovinesandbuttercups$nullifiedEffectTicks++;
    }
}
