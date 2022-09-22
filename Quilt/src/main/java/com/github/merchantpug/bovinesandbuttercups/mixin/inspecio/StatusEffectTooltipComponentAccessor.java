package com.github.merchantpug.bovinesandbuttercups.mixin.inspecio;

import io.github.queerbric.inspecio.tooltip.StatusEffectTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(StatusEffectTooltipComponent.class)
public interface StatusEffectTooltipComponentAccessor {
    @Accessor("list")
    List<MobEffectInstance> bovinesandbuttercups$getList();

    @Accessor("hidden")
    boolean bovinesandbuttercups$getHidden();

    @Accessor("multiplier")
    float bovinesandbuttercups$getMultiplier();

    @Invoker("getHiddenText")
    Component bovinesandbuttercups$invokeGetHiddenText();

    @Invoker("getHiddenTime")
    Component bovinesandbuttercups$invokeGetHiddenTime();
}
