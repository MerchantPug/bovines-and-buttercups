package net.merchantpug.bovinesandbuttercups.attachment.capability;

import net.merchantpug.bovinesandbuttercups.attachment.FlowerCowTargetAttachment;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetCapability {
    Bee provider;

    public FlowerCowTargetCapability(Bee provider) {
        this.provider = provider;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(FlowerCowTargetAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(FlowerCowTargetAttachment.ID.toString());
        if (legacyTag.contains("MoobloomTarget")) {
            setMoobloom(legacyTag.getUUID("MoobloomTarget"));
        }
    }

    public @Nullable UUID getMoobloom() {
        return this.provider.getData(BovineAttachments.MOOBLOOM_TARGET).get().getMoobloom();
    }

    public void setMoobloom(@Nullable UUID moobloom) {
        this.provider.getData(BovineAttachments.MOOBLOOM_TARGET).get().setMoobloom(moobloom);
    }
}
