package net.merchantpug.bovinesandbuttercups.attachment.capability;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.attachment.IBovineAttachment;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class SerializableAttachment<T extends IBovineAttachment> implements INBTSerializable<Tag> {

    public T attachment;

    public SerializableAttachment(T attachment) {
        this.attachment = attachment;
    }

    @Override
    public Tag serializeNBT() {
        return ((Codec<T>)attachment.getCodec()).encodeStart(NbtOps.INSTANCE, attachment).getOrThrow(false, BovinesAndButtercups.LOG::error);
    }

    @Override
    public void deserializeNBT(Tag tag) {
        this.attachment = ((Codec<T>)attachment.getCodec()).decode(NbtOps.INSTANCE, tag).getOrThrow(false, BovinesAndButtercups.LOG::error).getFirst();
    }

    public T get() {
        return attachment;
    }
}
