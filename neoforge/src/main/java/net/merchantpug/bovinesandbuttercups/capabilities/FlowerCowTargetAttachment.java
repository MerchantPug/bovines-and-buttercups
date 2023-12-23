package net.merchantpug.bovinesandbuttercups.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetAttachment implements IFlowerCowTargetAttachability, INBTSerializable<CompoundTag> {
    public UUID moobloom = null;
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (moobloom != null) {
            tag.putUUID("moobloom_target", moobloom);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("moobloom_target")) {
            setMoobloom(tag.getUUID("moobloom_target"));
        }
    }

    @Override
    public @Nullable UUID getMoobloom() {
        return moobloom;
    }

    @Override
    public void setMoobloom(@Nullable UUID moobloom) {
        this.moobloom = moobloom;
    }
}
