package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineEffects;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @ModifyVariable(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), argsOnly = true)
    private List<Component> bovinesandbuttercups$addLockdownDescription(List<Component> list) {
        if (!list.isEmpty() && list.get(0).contains(BovineEffects.LOCKDOWN.get().getDisplayName())) {
            List<Component> newList = new ArrayList<>(list);
            newList.remove(1);
            Services.COMPONENT.getLockdownMobEffects(Minecraft.getInstance().player).forEach(((statusEffect, duration) -> newList.add(statusEffect.getDisplayName().plainCopy().append(" ").append(MobEffectUtil.formatDuration(duration, 1.0F)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)))));
            return newList;
        }
        return list;
    }
}
