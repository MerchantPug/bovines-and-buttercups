package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.neoforge.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

public class MushroomCowTypeCapability implements IMushroomCowTypeAttachability {
    private final MushroomCow provider;

    public MushroomCowTypeCapability(MushroomCow provider) {
        this.provider = provider;
    }


    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(IMushroomCowTypeAttachability.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(IMushroomCowTypeAttachability.ID.toString());
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

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowType() {
        return this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).getMushroomCowType();
    }

    @Override
    public ResourceLocation getMushroomCowTypeKey() {
        return this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).getMushroomCowTypeKey();
    }

    @Override
    public boolean shouldAllowShearing() {
        return this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).shouldAllowShearing();
    }

    @Override
    public void setAllowShearing(boolean value) {
        this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).setAllowShearing(value);
    }

    @Override
    public void setMushroomType(ResourceLocation key) {
        this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).setMushroomType(key);


        if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.RED);
        }
    }

    @Override
    public ResourceLocation getPreviousMushroomTypeKey() {
        return this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).getPreviousMushroomTypeKey();
    }

    @Override
    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.provider.getData(BovineAttachments.MUSHROOM_COW_TYPE).setPreviousMushroomTypeKey(key);
    }
}