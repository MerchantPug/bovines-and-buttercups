package net.merchantpug.bovinesandbuttercups.attachment.api;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.attachment.MushroomCowTypeAttachment;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachmentTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MushroomCowTypeApi {
    private final MushroomCow provider;

    public MushroomCowTypeApi(MushroomCow provider) {
        this.provider = provider;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("cardinal_components", Tag.TAG_COMPOUND)) return;
        CompoundTag ccaTag = tag.getCompound("cardinal_components");
        if (!ccaTag.contains(MushroomCowTypeAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = ccaTag.getCompound(MushroomCowTypeAttachment.ID.toString());
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
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).getType();
    }

    public Optional<ResourceLocation> getTypeKey() {
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).getTypeKey();
    }

    public boolean shouldAllowShearing() {
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).shouldAllowShearing();
    }

    public void setAllowShearing(boolean value) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).setAllowShearing(value);
    }

    public void setMushroomType(ResourceLocation key) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).setType(key);


        if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            provider.setVariant(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            provider.setVariant(MushroomCow.MushroomType.RED);
        }
    }

    public Optional<ResourceLocation> getPreviousTypeKey() {
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).getPreviousTypeKey();
    }

    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.MOOSHROOM_TYPE, MushroomCowTypeAttachment::new).setPreviousTypeKey(key);
    }
}