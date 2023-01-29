package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;

public interface LockdownEffectComponent extends Component {
    Map<MobEffect, Integer> getLockdownMobEffects();
    void addLockdownMobEffect(MobEffect effect, int duration);
    void removeLockdownMobEffect(MobEffect effect);
    void setLockdownMobEffects(Map<MobEffect, Integer> map);
}
