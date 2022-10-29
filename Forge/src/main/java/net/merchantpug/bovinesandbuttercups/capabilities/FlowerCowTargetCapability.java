package net.merchantpug.bovinesandbuttercups.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface FlowerCowTargetCapability extends INBTSerializable<CompoundTag> {
    Capability<FlowerCowTargetCapabilityImpl> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Nullable UUID getMoobloom();
    void setMoobloom(@Nullable UUID moobloom);

    void sync();
}
