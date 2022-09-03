package com.github.merchantpug.bovinesandbuttercups.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectUtil {
    public static final Codec<MobEffectInstance> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            Registry.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier),
            Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInstance::isAmbient),
            Codec.BOOL.optionalFieldOf("visible", false).forGetter(MobEffectInstance::isVisible),
            Codec.BOOL.optionalFieldOf("show_icon", true).forGetter(MobEffectInstance::showIcon)
    ).apply(builder, MobEffectInstance::new));

    public static String formatDurationFromInt(int duration, float multiplier) {
        if (duration >= Short.MAX_VALUE) {
            return "**:**";
        }
        int i = Mth.floor((float)duration * multiplier);
        return StringUtil.formatTickDuration(i);
    }
}
