package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public interface MushroomCowTypeCapability extends INBTSerializable<CompoundTag> {
    ResourceLocation ID = BovinesAndButtercups.asResource("mooshroom_type");
    Capability<MushroomCowTypeCapabilityImpl> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});

    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType();
    ResourceLocation getMushroomCowTypeKey();
    void setMushroomType(ResourceLocation key);

    @Nullable ResourceLocation getPreviousMushroomTypeKey();
    void setPreviousMushroomTypeKey(@Nullable ResourceLocation key);

    boolean shouldAllowShearing();
    void setAllowShearing(boolean value);

    void sync();
}