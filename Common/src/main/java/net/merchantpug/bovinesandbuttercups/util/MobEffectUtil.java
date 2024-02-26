package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectUtil {
    public static final Codec<MobEffectInstance> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier),
            Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInstance::isAmbient),
            Codec.BOOL.optionalFieldOf("visible", false).forGetter(MobEffectInstance::isVisible),
            Codec.BOOL.optionalFieldOf("show_icon", true).forGetter(MobEffectInstance::showIcon)
    ).apply(builder, MobEffectInstance::new));

    public static Component formatDuration(int duration, float multiplier) {
        if (duration == -1) {
            return Component.translatable("effect.duration.infinite");
        } else {
            int i = Mth.floor((float)duration * multiplier);
            return Component.literal(StringUtil.formatTickDuration(i));
        }
    }
}
