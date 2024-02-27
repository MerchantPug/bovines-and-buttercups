package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketS2C;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;

public record SyncLockdownEffectAttachmentPacket(int entityId, LockdownEffectAttachment attachment) implements BovinePacketS2C {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_lockdown_effect_attachment");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId());
        buf.writeNbt(LockdownEffectAttachment.CODEC.encodeStart(NbtOps.INSTANCE, attachment()).getOrThrow(false, BovinesAndButtercups.LOG::error));
    }

    public static SyncLockdownEffectAttachmentPacket read(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Tag tag = buf.readNbt();
        return new SyncLockdownEffectAttachmentPacket(entityId, LockdownEffectAttachment.CODEC.decode(NbtOps.INSTANCE, tag).getOrThrow(false, BovinesAndButtercups.LOG::error).getFirst());
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId());
            if (!(entity instanceof Bee bee)) {
                return;
            }
            bee.setAttached(BovineAttachmentTypes.LOCKDOWN_EFFECT, attachment());
        });
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
