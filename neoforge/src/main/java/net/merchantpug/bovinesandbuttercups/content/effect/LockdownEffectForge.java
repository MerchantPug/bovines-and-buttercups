package net.merchantpug.bovinesandbuttercups.content.effect;

import net.merchantpug.bovinesandbuttercups.client.LockdownClientEffectExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public class LockdownEffectForge extends LockdownEffect {
    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new LockdownClientEffectExtensions());
    }
}