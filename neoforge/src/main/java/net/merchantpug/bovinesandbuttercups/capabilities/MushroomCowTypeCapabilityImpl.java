package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.neoforge.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncMushroomCowTypePacket;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MushroomCowTypeCapabilityImpl implements MushroomCowTypeCapability, ICapabilityProvider, INBTSerializable<CompoundTag> {
    private ResourceLocation typeId;
    private @Nullable ResourceLocation previousTypeId;
    private ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> type;
    private final MushroomCow provider;
    private boolean allowShearing = true;
    private final LazyOptional<MushroomCowTypeCapabilityImpl> lazyOptional = LazyOptional.of(() -> this);

    public MushroomCowTypeCapabilityImpl(MushroomCow provider) {
        this.provider = provider;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.typeId != null) {
            tag.putString("Type", this.typeId.toString());
        }
        if (this.previousTypeId != null) {
            tag.putString("PreviousType", this.previousTypeId.toString());
        }
        tag.putBoolean("AllowShearing", this.allowShearing);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            this.setMushroomType(ResourceLocation.tryParse(tag.getString("Type")));
        }
        if (tag.contains("PreviousType", Tag.TAG_STRING)) {
            this.setPreviousMushroomTypeKey(ResourceLocation.tryParse(tag.getString("PreviousType")));
        }
        if (tag.contains("AllowShearing", Tag.TAG_BYTE)) {
            this.setAllowShearing(tag.getBoolean("AllowShearing"));
        }
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowType() {
        try {
            if (this.typeId != null) {
                if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(typeId) && this.type != null && this.type.configuration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get()).configuration()) {
                    this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get());
                    return this.type;
                } else if (this.type != null) {
                    return this.type;
                } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(typeId)) {
                    this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get());
                    return this.type;
                }
                BovinesAndButtercups.LOG.warn("Could not find type '{}' from mooshroom at position {}. Setting type to 'bovinesandbuttercups:missing_mooshroom'.", this.typeId, provider.position());
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            BovinesAndButtercups.LOG.warn("Could not get type '{}' from mooshroom at position {}. Setting type to 'bovinesandbuttercups:missing_mooshroom'. {}", this.typeId, provider.position(), e.getMessage());
            return this.type;
        }
    }

    @Override
    public ResourceLocation getMushroomCowTypeKey() {
        return this.typeId;
    }

    @Override
    public boolean shouldAllowShearing() {
        return allowShearing;
    }

    @Override
    public void setAllowShearing(boolean value) {
        allowShearing = value;
    }

    @Override
    public void setMushroomType(ResourceLocation key) {
        this.typeId = key;

        if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.RED);
        }

        this.sync();

        this.getMushroomCowType();
    }

    @Override
    public ResourceLocation getPreviousMushroomTypeKey() {
        return this.previousTypeId;
    }

    @Override
    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.previousTypeId = key;
        this.sync();
    }

    @Override
    public void sync() {
        if (provider.level().isClientSide || this.typeId == null) return;
        BovinePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> provider), new SyncMushroomCowTypePacket(provider.getId(), this.typeId, this.previousTypeId, this.allowShearing));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return MushroomCowTypeCapability.INSTANCE.orEmpty(cap, this.lazyOptional);
    }
}