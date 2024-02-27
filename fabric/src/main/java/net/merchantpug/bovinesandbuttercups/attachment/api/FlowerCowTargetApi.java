package net.merchantpug.bovinesandbuttercups.attachment.api;

import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachmentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetApi {
    Bee provider;

    public FlowerCowTargetApi(Bee provider) {
        this.provider = provider;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("cardinal_components", Tag.TAG_COMPOUND)) return;
        CompoundTag ccaTag = tag.getCompound("cardinal_components");
        if (!ccaTag.contains(FlowerCowTargetAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = ccaTag.getCompound(FlowerCowTargetAttachment.ID.toString());
        if (legacyTag.contains("MoobloomTarget")) {
            setMoobloom(legacyTag.getUUID("MoobloomTarget"));
        }
    }

    public @Nullable UUID getMoobloom() {
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOBLOOM_TARGET, FlowerCowTargetAttachment::new).getMoobloom();
    }

    public void setMoobloom(@Nullable UUID moobloom) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOBLOOM_TARGET, FlowerCowTargetAttachment::new).setMoobloom(moobloom);
    }
}
