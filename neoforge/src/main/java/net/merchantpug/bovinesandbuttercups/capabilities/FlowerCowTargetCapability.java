package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface FlowerCowTargetCapability extends INBTSerializable<CompoundTag> {
    ResourceLocation ID = BovinesAndButtercups.asResource("moobloom_target");
    Capability<FlowerCowTargetCapabilityImpl> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    @Nullable UUID getMoobloom();
    void setMoobloom(@Nullable UUID moobloom);

    void sync();
}
