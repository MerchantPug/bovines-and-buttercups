package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetCapability implements IFlowerCowTargetAttachability {
    Bee provider;

    public FlowerCowTargetCapability(Bee provider) {
        this.provider = provider;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(IFlowerCowTargetAttachability.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(IFlowerCowTargetAttachability.ID.toString());
        if (legacyTag.contains("MoobloomTarget")) {
            setMoobloom(legacyTag.getUUID("MoobloomTarget"));
        }
    }

    @Override
    public @Nullable UUID getMoobloom() {
        return this.provider.getData(BovineAttachments.MOOBLOOM_TARGET).getMoobloom();
    }

    @Override
    public void setMoobloom(@Nullable UUID moobloom) {
        this.provider.getData(BovineAttachments.MOOBLOOM_TARGET).setMoobloom(moobloom);
    }
}
