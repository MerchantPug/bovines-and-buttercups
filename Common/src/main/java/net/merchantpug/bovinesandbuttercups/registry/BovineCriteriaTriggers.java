package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.MoobloomMutationTrigger;
import net.merchantpug.bovinesandbuttercups.platform.Services;

public class BovineCriteriaTriggers {
    public static final MoobloomMutationTrigger MOOBLOOM_MUTATION = (MoobloomMutationTrigger)Services.REGISTRY.registerCriteria(new MoobloomMutationTrigger());

    public static void register() {

    }
}
