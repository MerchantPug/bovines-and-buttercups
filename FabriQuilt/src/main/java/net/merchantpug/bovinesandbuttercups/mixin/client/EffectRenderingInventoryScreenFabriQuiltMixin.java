package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.access.MobEffectInstanceAccess;
import net.merchantpug.bovinesandbuttercups.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(EffectRenderingInventoryScreen.class)
public abstract class EffectRenderingInventoryScreenFabriQuiltMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    public EffectRenderingInventoryScreenFabriQuiltMixin(T handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Inject(method = "renderIcons", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$drawOverridenEffectSprite(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstanceIterable, boolean large, CallbackInfo ci, MobEffectTextureManager mobEffectTextureManager, int i, Iterator iterator, MobEffectInstance mobEffectInstance, MobEffect mobEffect, TextureAtlasSprite sprite) {
        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect)) return;

        List<Map.Entry<MobEffect, Integer>> statusEffectList = ((MobEffectInstanceAccess)mobEffectInstance).bovinesandbuttercups$getLockedEffects().entrySet().stream().toList();

        if (statusEffectList.isEmpty()) return;
        int lockdownEffectIndex = minecraft.player.tickCount / (160 / statusEffectList.size()) % statusEffectList.size();

        MobEffect mobEffect1 = statusEffectList.get(lockdownEffectIndex).getKey();

        TextureAtlasSprite additionalSprite = mobEffectTextureManager.get(mobEffect1);
        RenderSystem.setShaderTexture(0, additionalSprite.atlas().getId());
        InventoryScreen.blit(poseStack, x + (large ? 6 : 7), i + 7, this.getBlitOffset(), 18, 18, additionalSprite);
    }

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;renderLabels(Lcom/mojang/blaze3d/vertex/PoseStack;IILjava/lang/Iterable;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$drawEffectDescriptionWhenHoveredOver(PoseStack poseStack, int mouseX, int mouseY, CallbackInfo ci, int i, int j, Collection collection, boolean bl, int k, Iterable<MobEffectInstance> iterable) {
        if (mouseX >= i && mouseX <= i + 119) {
            int l = this.topPos;
            MobEffectInstance mobEffectInstance = null;
            for (MobEffectInstance mobEffectInstance2 : iterable) {
                if (mouseY >= l && mouseY <= l + k && mobEffectInstance2.getEffect() instanceof LockdownEffect) {
                    mobEffectInstance = mobEffectInstance2;
                }
                l += k;
            }
            if (mobEffectInstance != null) {
                List<Component> list = new ArrayList<>();
                list.add(this.getEffectName(mobEffectInstance));
                ((MobEffectInstanceAccess)mobEffectInstance).bovinesandbuttercups$getLockedEffects().forEach(((statusEffect, duration) -> {
                    list.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDurationFromInt(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                }));
                this.renderTooltip(poseStack, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

    @Unique MobEffectInstance bovinesandbuttercups$capturedMobEffectInstance;

    @Inject(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$getStatusEffectInstance(PoseStack matrices, int mouseX, int mouseY, CallbackInfo ci, int i, int j, Collection collection, boolean bl, int k, Iterable iterable, int l, MobEffectInstance mobEffectInstance, List list) {
        this.bovinesandbuttercups$capturedMobEffectInstance = mobEffectInstance;
    }

    @ModifyArg(method = "renderEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"))
    private List<Component> bovinesandbuttercups$drawEffectDescriptions(List<Component> list) {
        if (!(bovinesandbuttercups$capturedMobEffectInstance.getEffect() instanceof LockdownEffect)) return list;
        List<Component> newList = new ArrayList<>();
        newList.add(this.getEffectName(bovinesandbuttercups$capturedMobEffectInstance));
        ((MobEffectInstanceAccess)bovinesandbuttercups$capturedMobEffectInstance).bovinesandbuttercups$getLockedEffects().forEach(((statusEffect, duration) -> {
            newList.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDurationFromInt(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }));
        return newList;
    }
}
