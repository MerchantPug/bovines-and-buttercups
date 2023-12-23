package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.LockEffectTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.MutationTrigger;
import net.merchantpug.bovinesandbuttercups.content.advancements.criterion.PreventEffectTrigger;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class BovineCriteriaTriggers {
    public static final RegistrationProvider<CriterionTrigger<?>> CRITERION_TRIGGERS = RegistrationProvider.get(Registries.TRIGGER_TYPE, BovinesAndButtercups.MOD_ID);

    public static final Supplier<LockEffectTrigger> LOCK_EFFECT = CRITERION_TRIGGERS.register(LockEffectTrigger.ID.getPath(), LockEffectTrigger::new);
    public static final Supplier<PreventEffectTrigger> PREVENT_EFFECT = CRITERION_TRIGGERS.register(PreventEffectTrigger.ID.getPath(), PreventEffectTrigger::new);
    public static final Supplier<MutationTrigger> MUTATION = CRITERION_TRIGGERS.register(MutationTrigger.ID.getPath(), MutationTrigger::new);

    public static void register() {

    }
}
