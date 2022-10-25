package net.merchantpug.bovinesandbuttercups.effect;

import net.merchantpug.bovinesandbuttercups.client.LockdownClientEffectExtensions;

public class LockdownEffectForge extends LockdownEffect {
    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientMobEffectExtensions> consumer) {
        consumer.accept(new LockdownClientEffectExtensions());
    }
}