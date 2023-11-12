package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.LockEffectTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.MutationTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.PreventEffectTrigger;
import net.merchantpug.bovinesandbuttercups.platform.Services;

public class BovineCriteriaTriggers {
    public static final LockEffectTrigger LOCK_EFFECT = (LockEffectTrigger) Services.REGISTRY.registerCriteria(LockEffectTrigger.ID, new LockEffectTrigger());
    public static final PreventEffectTrigger PREVENT_EFFECT = (PreventEffectTrigger) Services.REGISTRY.registerCriteria(PreventEffectTrigger.ID, new PreventEffectTrigger());
    public static final MutationTrigger MUTATION = (MutationTrigger) Services.REGISTRY.registerCriteria(MutationTrigger.ID, new MutationTrigger());

    public static void register() {

    }
}
