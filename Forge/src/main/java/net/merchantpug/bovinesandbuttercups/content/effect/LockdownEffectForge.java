package net.merchantpug.bovinesandbuttercups.content.effect;

import net.merchantpug.bovinesandbuttercups.client.LockdownClientEffectExtensions;
import net.merchantpug.bovinesandbuttercups.content.effect.LockdownEffect;

public class LockdownEffectForge extends LockdownEffect {
    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientMobEffectExtensions> consumer) {
        consumer.accept(new LockdownClientEffectExtensions());
    }
}