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

    @Inject(method = "renderBackgrounds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$overlayLockdownBorder(PoseStack poseStack, int x, int height, Iterable<MobEffectInstance> mobEffectInstances, boolean wide, CallbackInfo ci, int i, Iterator var7, MobEffectInstance mobEffectInstance) {
        if (this.minecraft == null || this.minecraft.player == null) return;

        List<MobEffectInstance> lockdownEffectInstance = this.minecraft.player.getActiveEffects().stream().filter(instance -> instance.getEffect() instanceof LockdownEffect).toList();

        if (lockdownEffectInstance.isEmpty()) return;

        if (!(mobEffectInstance.getEffect() instanceof LockdownEffect) && Services.COMPONENT.getLockdownMobEffects(this.minecraft.player).entrySet().stream().anyMatch(instance -> instance.getKey() == mobEffectInstance.getEffect())) {
            RenderSystem.setShaderTexture(0, BovinesAndButtercups.asResource("textures/gui/container/lockdown_frame.png"));
            blit(poseStack, x, i,0, 0, 0, 32, 32, 64, 32);
            RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "TAIL"))
    private void bovinesandbuttercups$drawEffectDescriptionWhenHoveredOver(PoseStack poseStack, int i, int j, Iterable<MobEffectInstance> iterable, CallbackInfo ci) {
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
                List<Component> list = List.of(this.getEffectName(mobEffectInstance), MobEffectUtil.formatDuration(mobEffectInstance, 1.0F));
                this.renderTooltip(poseStack, list, Optional.empty(), mouseX, mouseY);
            }
        }
    }

}
