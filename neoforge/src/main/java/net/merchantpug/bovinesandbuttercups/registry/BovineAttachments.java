package net.merchantpug.bovinesandbuttercups.registry;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.capabilities.IFlowerCowTargetAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.ILockdownEffectAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.IMushroomCowTypeAttachability;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.capabilities.MushroomCowTypeAttachment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class BovineAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BovinesAndButtercups.MOD_ID);

    public static final Supplier<AttachmentType<MushroomCowTypeAttachment>> MUSHROOM_COW_TYPE = ATTACHMENT_TYPES.register(
            IMushroomCowTypeAttachability.ID.getPath(), () -> AttachmentType.serializable(MushroomCowTypeAttachment::new).build());
    public static final Supplier<AttachmentType<LockdownEffectAttachment>> LOCKDOWN_EFFECT = ATTACHMENT_TYPES.register(
            ILockdownEffectAttachability.ID.getPath(), () -> AttachmentType.serializable(LockdownEffectAttachment::new).build());
    public static final Supplier<AttachmentType<FlowerCowTargetAttachment>> MOOBLOOM_TARGET = ATTACHMENT_TYPES.register(
            IFlowerCowTargetAttachability.ID.getPath(), () -> AttachmentType.serializable(FlowerCowTargetAttachment::new).build());

    public static void init(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

}
