package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.capabilities.FlowerCowTargetCapability;
import net.merchantpug.bovinesandbuttercups.capabilities.IFlowerCowTargetAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.ILockdownEffectAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.IMushroomCowTypeAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.capabilities.MushroomCowTypeCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;

public class BovineCapabilities {

    public static final EntityCapability<LockdownEffectCapability, Void> LOCKDOWN_EFFECT = EntityCapability.createVoid(
            ILockdownEffectAttachability.ID, LockdownEffectCapability.class);
    public static final EntityCapability<MushroomCowTypeCapability, Void> MOOSHROOM_TYPE = EntityCapability.createVoid(
            IMushroomCowTypeAttachability.ID, MushroomCowTypeCapability.class);
    public static final EntityCapability<FlowerCowTargetCapability, Void> MOOBLOOM_TARGET = EntityCapability.createVoid(
            IFlowerCowTargetAttachability.ID, FlowerCowTargetCapability.class);

}
