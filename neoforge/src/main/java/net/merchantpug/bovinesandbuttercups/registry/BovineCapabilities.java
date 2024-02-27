package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.capability.FlowerCowTargetCapability;
import net.merchantpug.bovinesandbuttercups.attachment.capability.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.attachment.capability.MushroomCowTypeCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class BovineCapabilities {

    public static final EntityCapability<LockdownEffectCapability, Void> LOCKDOWN_EFFECT = EntityCapability.createVoid(
            LockdownEffectAttachment.ID, LockdownEffectCapability.class);
    public static final EntityCapability<MushroomCowTypeCapability, Void> MOOSHROOM_TYPE = EntityCapability.createVoid(
            MushroomCowTypeAttachment.ID, MushroomCowTypeCapability.class);
    public static final EntityCapability<FlowerCowTargetCapability, Void> MOOBLOOM_TARGET = EntityCapability.createVoid(
            FlowerCowTargetAttachment.ID, FlowerCowTargetCapability.class);

}
