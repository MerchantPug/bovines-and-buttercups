package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.capability.SerializableAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BovineAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BovinesAndButtercups.MOD_ID);

    public static final Supplier<AttachmentType<SerializableAttachment<MushroomCowTypeAttachment>>> MOOSHROOM_TYPE = ATTACHMENT_TYPES.register(
            MushroomCowTypeAttachment.ID.getPath(), () -> AttachmentType.serializable(() -> new SerializableAttachment<>(new MushroomCowTypeAttachment())).build());
    public static final Supplier<AttachmentType<SerializableAttachment<LockdownEffectAttachment>>> LOCKDOWN_EFFECT = ATTACHMENT_TYPES.register(
            LockdownEffectAttachment.ID.getPath(), () -> AttachmentType.serializable(() -> new SerializableAttachment<>(new LockdownEffectAttachment())).build());
    public static final Supplier<AttachmentType<SerializableAttachment<FlowerCowTargetAttachment>>> MOOBLOOM_TARGET = ATTACHMENT_TYPES.register(
            FlowerCowTargetAttachment.ID.getPath(), () -> AttachmentType.serializable(() -> new SerializableAttachment<>(new FlowerCowTargetAttachment())).build());

    public static void init(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

}
