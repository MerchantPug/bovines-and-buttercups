package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.LockEffectTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.MoobloomMutationTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.PreventEffectTrigger;
import net.merchantpug.bovinesandbuttercups.platform.Services;

public class BovineCriteriaTriggers {
    public static final LockEffectTrigger LOCK_EFFECT = (LockEffectTrigger) Services.REGISTRY.registerCriteria(new LockEffectTrigger());
    public static final PreventEffectTrigger PREVENT_EFFECT = (PreventEffectTrigger) Services.REGISTRY.registerCriteria(new PreventEffectTrigger());
    public static final MoobloomMutationTrigger MOOBLOOM_MUTATION = (MoobloomMutationTrigger) Services.REGISTRY.registerCriteria(new MoobloomMutationTrigger());

    public static void register() {

    }
}
