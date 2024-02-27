package net.merchantpug.bovinesandbuttercups.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

import java.util.HashMap;
import java.util.Map;

public class LockdownEffectAttachment implements IBovineAttachment {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("lockdown");

    public static final Codec<LockdownEffectAttachment> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            MobEffectUtil.LOCKDOWN_CODEC.fieldOf("locked_effects").orElse(new HashMap<>()).forGetter(LockdownEffectAttachment::getLockdownMobEffects)
    ).apply(inst, LockdownEffectAttachment::new));

    public static final Map<MobEffect, Integer> NO_EFFECTS = new HashMap<>();

    private Map<MobEffect, Integer> lockdownEffects;

    public LockdownEffectAttachment() {
        this(new HashMap<>());
    }

    public LockdownEffectAttachment(Map<MobEffect, Integer> lockdownEffects) {
        this.lockdownEffects = lockdownEffects;
    }

    public Map<MobEffect, Integer> getLockdownMobEffects() {
        return lockdownEffects;
    }

    public void addLockdownMobEffect(MobEffect effect, int duration) {
        lockdownEffects.put(effect, duration);
    }

    public void removeLockdownMobEffect(MobEffect effect) {
        lockdownEffects.remove(effect);
    }

    public void setLockdownMobEffects(Map<MobEffect, Integer> map) {
        lockdownEffects = map;
    }

    @Override
    public Codec<?> getCodec() {
        return CODEC;
    }
}