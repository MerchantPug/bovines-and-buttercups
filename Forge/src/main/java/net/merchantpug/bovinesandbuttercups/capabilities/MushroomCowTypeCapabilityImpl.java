package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.forge.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncMushroomCowTypePacket;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class MushroomCowTypeCapabilityImpl implements MushroomCowTypeCapability {
    ResourceLocation typeId;
    @Nullable ResourceLocation previousTypeId;
    ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> type;
    MushroomCow provider;

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
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId) && this.type != null && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE).getConfiguration()) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
                return this.type;
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId)) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
                return this.type;
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
            BovinesAndButtercups.LOG.warn("Could not find type '{}' from mooshroom at position {}. Setting type to 'bovinesandbuttercups:missing_mooshroom'.", this.typeId, provider.position());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
            BovinesAndButtercups.LOG.warn("Could not get type '{}' from mooshroom at position {}. Setting type to 'bovinesandbuttercups:missing_mooshroom'. {}", this.typeId, provider.position(), e.getMessage());
            return this.type;
        }
    }

    @Override
    public ResourceLocation getMushroomCowTypeKey() {
        return this.typeId;
    }

    @Override
    public void setMushroomType(ResourceLocation key) {
        this.typeId = key;

        if (BovineRegistryUtil.getConfiguredCowTypeKey(provider.level, Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(provider.level, Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.RED);
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
        if (provider.level.isClientSide || this.typeId == null) return;
        BovinePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> provider), new SyncMushroomCowTypePacket(provider.getId(), this.typeId, this.previousTypeId));
    }
}