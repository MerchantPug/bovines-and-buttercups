package com.github.merchantpug.bovinesandbuttercups.util;

import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;

public class MobEffectUtil {
    public static String formatDurationFromInt(int duration, float multiplier) {
        if (duration >= Short.MAX_VALUE) {
            return "**:**";
        }
        int i = Mth.floor((float)duration * multiplier);
        return StringUtil.formatTickDuration(i);
    }
}
