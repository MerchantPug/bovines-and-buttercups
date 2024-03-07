package net.merchantpug.bovinesandbuttercups.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MobEffectUtil {
    public static final Codec<MobEffectInstance> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            BuiltInRegistries.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
            Codec.INT.fieldOf("duration").forGetter(MobEffectInstance::getDuration),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(MobEffectInstance::getAmplifier),
            Codec.BOOL.optionalFieldOf("ambient", false).forGetter(MobEffectInstance::isAmbient),
            Codec.BOOL.optionalFieldOf("visible", false).forGetter(MobEffectInstance::isVisible),
            Codec.BOOL.optionalFieldOf("show_icon", true).forGetter(MobEffectInstance::showIcon)
    ).apply(builder, MobEffectInstance::new));

    public static final Codec<Map<MobEffect, Integer>> LOCKDOWN_CODEC = Codec.simpleMap(BuiltInRegistries.MOB_EFFECT.byNameCodec(), Codec.INT, Keyable.forStrings(() -> Stream.of("id", "duration"))).codec().xmap(HashMap::new, map -> map);
}
