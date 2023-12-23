package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class MushroomCowTypeAttachment implements IMushroomCowTypeAttachability, INBTSerializable<CompoundTag> {
    private ResourceLocation typeId;
    private @Nullable ResourceLocation previousTypeId;
    private ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> type;
    private boolean allowShearing = true;

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.typeId != null) {
            tag.putString("type", this.typeId.toString());
        }
        if (this.previousTypeId != null) {
            tag.putString("previous_type", this.previousTypeId.toString());
        }
        tag.putBoolean("allow_shearing", this.allowShearing);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("type", Tag.TAG_STRING)) {
            this.setMushroomType(ResourceLocation.tryParse(tag.getString("type")));
        }
        if (tag.contains("previous_type", Tag.TAG_STRING)) {
            this.setPreviousMushroomTypeKey(ResourceLocation.tryParse(tag.getString("previous_type")));
        }
        if (tag.contains("allow_shearing", Tag.TAG_BYTE)) {
            this.setAllowShearing(tag.getBoolean("allow_shearing"));
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
                BovinesAndButtercups.LOG.warn("Could not find type '{}' from mooshroom. Setting type to 'bovinesandbuttercups:missing_mooshroom'.", this.typeId);
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            BovinesAndButtercups.LOG.warn("Could not get type '{}' from mooshroom. Setting type to 'bovinesandbuttercups:missing_mooshroom'. {}", this.typeId, e.getMessage());
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
        this.getMushroomCowType();
    }

    @Override
    public ResourceLocation getPreviousMushroomTypeKey() {
        return this.previousTypeId;
    }

    @Override
    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.previousTypeId = key;
    }

}