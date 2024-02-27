package net.merchantpug.bovinesandbuttercups.attachment.capability;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.neoforge.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MushroomCowTypeCapability {
    private final MushroomCow provider;

    public MushroomCowTypeCapability(MushroomCow provider) {
        this.provider = provider;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(MushroomCowTypeAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(MushroomCowTypeAttachment.ID.toString());
        if (legacyTag.contains("Type", Tag.TAG_STRING)) {
            this.setMushroomType(ResourceLocation.tryParse(legacyTag.getString("Type")));
        }
        if (legacyTag.contains("PreviousType", Tag.TAG_STRING)) {
            this.setPreviousMushroomTypeKey(ResourceLocation.tryParse(legacyTag.getString("PreviousType")));
        }
        if (legacyTag.contains("AllowShearing", Tag.TAG_BYTE)) {
            this.setAllowShearing(legacyTag.getBoolean("AllowShearing"));
        }
    }

    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getType() {
        return this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().getType();
    }

    public Optional<ResourceLocation> getTypeKey() {
        return this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().getTypeKey();
    }

    public boolean shouldAllowShearing() {
        return this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().shouldAllowShearing();
    }

    public void setAllowShearing(boolean value) {
        this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().setAllowShearing(value);
    }

    public void setMushroomType(ResourceLocation key) {
        this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().setType(key);


        if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.RED);
        }
    }

    public Optional<ResourceLocation> getPreviousTypeKey() {
        return this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().getPreviousTypeKey();
    }

    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.provider.getData(BovineAttachments.MOOSHROOM_TYPE).get().setPreviousTypeKey(key);
    }
}