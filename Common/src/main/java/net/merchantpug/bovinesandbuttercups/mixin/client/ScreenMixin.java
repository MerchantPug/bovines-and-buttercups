package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Screen.class)
public class ScreenMixin {
    @ModifyVariable(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), argsOnly = true)
    private List<Component> bovinesandbuttercups$addLockdownDescription(List<Component> list) {
        if (!list.isEmpty() && list.get(0).contains(BovineEffects.LOCKDOWN.get().getDisplayName())) {
            List<Component> newList = new ArrayList<>(list);
            newList.remove(1);
            Services.COMPONENT.getLockdownMobEffects(Minecraft.getInstance().player).forEach(((statusEffect, duration) -> newList.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDurationFromInt(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)))));
            return newList;
        }
        return list;
    }
}
