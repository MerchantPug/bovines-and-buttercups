package net.merchantpug.bovinesandbuttercups.mixin.quilt.inspecio;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Pseudo
@Mixin(targets = "io.github.queerbric.inspecio.tooltip.StatusEffectTooltipComponent", remap = false)
public interface StatusEffectTooltipComponentAccessor {
    @Accessor(value = "list", remap = false)
    List<MobEffectInstance> bovinesandbuttercups$getList();

    @Accessor(value = "hidden", remap = false)
    boolean bovinesandbuttercups$getHidden();

    @Accessor(value = "multiplier", remap = false)
    float bovinesandbuttercups$getMultiplier();

    @Invoker(value = "getHiddenText", remap = false)
    Component bovinesandbuttercups$invokeGetHiddenText();

    @Invoker(value = "getHiddenTime", remap = false)
    Component bovinesandbuttercups$invokeGetHiddenTime();
}
