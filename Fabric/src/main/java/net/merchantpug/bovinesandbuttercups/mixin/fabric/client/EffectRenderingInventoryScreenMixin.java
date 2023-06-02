package net.merchantpug.bovinesandbuttercups.mixin.fabric.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
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

    @Inject(method = "renderIcons", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$drawOverridenEffectSprite(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstanceIterable, boolean large, CallbackInfo ci, MobEffectTextureManager mobEffectTextureManager, int i, Iterator iterator, MobEffectInstance mobEffectInstance, MobEffect mobEffect, TextureAtlasSprite sprite) {
        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect)) return;

        List<Map.Entry<MobEffect, Integer>> statusEffectList = Services.COMPONENT.getLockdownMobEffects(minecraft.player).entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        MobEffect mobEffect1 = statusEffectList.get(lockdownEffectIndex).getKey();

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(mobEffect1);
        RenderSystem.setShaderTexture(0, additionalSprite.atlasLocation());
        InventoryScreen.blit(poseStack, x + (large ? 6 : 7), i + 7, 0, 18, 18, additionalSprite);
    }

}
