package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowTypeRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowTypeCapabilityImpl implements MushroomCowTypeCapability {
    ResourceLocation typeId;
    ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> type;
    MushroomCow provider;

    public MushroomCowTypeCapabilityImpl(MushroomCow provider) {
        this.provider = provider;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.typeId == null) {
            if (this.provider.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                this.typeId = BovinesAndButtercups.asResource("brown_mushroom");
            } else if (this.provider.getMushroomType() == MushroomCow.MushroomType.RED) {
                this.typeId = BovinesAndButtercups.asResource("red_mushroom");
            }
        }

        tag.putString("type", this.typeId.toString());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("type", Tag.TAG_STRING)) {
            this.typeId = ResourceLocation.tryParse(tag.getString("type"));
        }
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowType() {
        try {
            if (ConfiguredCowTypeRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId) && this.type.getConfiguration() != ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE).getConfiguration()) {
                return ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
            } else if (this.type != null) {
                return this.type;
            } else if (ConfiguredCowTypeRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId)) {
                return ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
            }
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
            return this.type;
        } catch (Exception e) {
            this.type = ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
            return this.type;
        }
    }

    public void setMushroomCowType(ResourceLocation key) {
        this.typeId = key;
    }
}
