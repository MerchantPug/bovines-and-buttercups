package net.merchantpug.bovinesandbuttercups.registry;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;

public class BovineAttachmentTypes {
    public static final AttachmentType<MushroomCowTypeAttachment> MOOSHROOM_TYPE = AttachmentRegistry.<MushroomCowTypeAttachment>builder()
            .persistent(MushroomCowTypeAttachment.CODEC)
            .initializer(MushroomCowTypeAttachment::new)
            .buildAndRegister(MushroomCowTypeAttachment.ID);
    public static final AttachmentType<LockdownEffectAttachment> LOCKDOWN_EFFECT = AttachmentRegistry.<LockdownEffectAttachment>builder()
            .persistent(LockdownEffectAttachment.CODEC)
            .initializer(LockdownEffectAttachment::new)
            .buildAndRegister(LockdownEffectAttachment.ID);
    public static final AttachmentType<FlowerCowTargetAttachment> MOOBLOOM_TARGET = AttachmentRegistry.<FlowerCowTargetAttachment>builder()
            .persistent(FlowerCowTargetAttachment.CODEC)
            .initializer(FlowerCowTargetAttachment::new)
            .buildAndRegister(FlowerCowTargetAttachment.ID);
}
