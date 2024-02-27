package net.merchantpug.bovinesandbuttercups.registry;

import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.api.FlowerCowTargetApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.LockdownEffectApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.MushroomCowTypeApi;

public class BovineEntityApis {
    public static final EntityApiLookup<MushroomCowTypeApi, Void> MOOSHROOM_TYPE = EntityApiLookup.get(MushroomCowTypeAttachment.ID, MushroomCowTypeApi.class, Void.class);
    public static final EntityApiLookup<LockdownEffectApi, Void> LOCKDOWN_EFFECTS = EntityApiLookup.get(LockdownEffectAttachment.ID, LockdownEffectApi.class, Void.class);
    public static final EntityApiLookup<FlowerCowTargetApi, Void> MOOBLOOM_TARGET = EntityApiLookup.get(FlowerCowTargetAttachment.ID, FlowerCowTargetApi.class, Void.class);
}
