package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;

public interface MushroomCowTypeCapability extends INBTSerializable<CompoundTag> {
    Capability<MushroomCowTypeCapabilityImpl> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType();
    void setMushroomCowType(ResourceLocation key);
}
