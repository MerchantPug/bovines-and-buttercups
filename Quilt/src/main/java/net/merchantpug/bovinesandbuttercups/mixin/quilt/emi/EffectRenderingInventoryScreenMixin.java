package net.merchantpug.bovinesandbuttercups.mixin.quilt.emi;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(value = EffectRenderingInventoryScreen.class, priority = 1001)
public abstract class EffectRenderingInventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Shadow protected abstract Component getEffectName(MobEffectInstance mobEffectInstance);

    public EffectRenderingInventoryScreenMixin(T abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "emi$drawCenteredEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;renderLabels(Lcom/mojang/blaze3d/vertex/PoseStack;IILjava/lang/Iterable;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$drawCenteredEffectDescriptionWhenHoveredOver(PoseStack poseStack, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir, Collection<MobEffectInstance> effects, int size, boolean wide, int y, int xOff, int width, int x, MobEffectInstance hovered, int restoreY, Iterator it, MobEffectInstance inst, int ew) {
        if (mouseX >= x && mouseX < x + ew && mouseY >= y && mouseY < y + 32) {
            if (inst.getEffect() instanceof LockdownEffect) {
                List<Component> lockComponentList = new ArrayList<>();
                lockComponentList.add(this.getEffectName(inst));
                Services.COMPONENT.getLockdownMobEffects(minecraft.player).forEach(((statusEffect, duration) -> {
                    lockComponentList.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDurationFromInt(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
                }));
                this.renderTooltip(poseStack, lockComponentList, Optional.empty(), mouseX, mouseY);
            }
        }
    }

    @Unique
    private MobEffectInstance bovinesandbuttercups$emiCapturedMobEffectInstance;

    @Inject(method = "emi$drawCenteredEffects", at = @At(value = "INVOKE", target = "java/util/List.of(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$getEmiStatusEffectInstance(PoseStack poseStack, int mouseX, int mouseY, CallbackInfoReturnable<Boolean> cir, Collection effects, int size, boolean wide, int y, int xOff, int width, int x, MobEffectInstance hovered) {
        this.bovinesandbuttercups$emiCapturedMobEffectInstance = hovered;
    }

    @ModifyArg(method = "emi$drawCenteredEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EffectRenderingInventoryScreen;renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V"))
    private List<Component> bovinesandbuttercups$drawCenteredEffectDescriptions(List<Component> list) {
        if (!(bovinesandbuttercups$emiCapturedMobEffectInstance.getEffect() instanceof LockdownEffect)) return list;
        List<Component> newList = new ArrayList<>();
        newList.add(this.getEffectName(bovinesandbuttercups$emiCapturedMobEffectInstance));
        Services.COMPONENT.getLockdownMobEffects(minecraft.player).forEach(((statusEffect, duration) -> {
            newList.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDurationFromInt(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }));
        return newList;
    }
}
