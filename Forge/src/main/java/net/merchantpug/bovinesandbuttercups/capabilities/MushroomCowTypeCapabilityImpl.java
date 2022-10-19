package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.CowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncMushroomCowTypePacket;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
            tag.putString("type", this.typeId.toString());
        }
        if (this.previousTypeId != null) {
            tag.putString("previousType", this.previousTypeId.toString());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("type", Tag.TAG_STRING)) {
            this.setMushroomType(ResourceLocation.tryParse(tag.getString("type")));
        }
        if (tag.contains("previousType")) {
            this.setPreviousMushroomTypeKey(ResourceLocation.tryParse(tag.getString("previousType")));
        }
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId) && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE).getConfiguration()) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
                return this.type;
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId)) {
                this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
                return this.type;
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE);
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

        if (BovineRegistryUtil.getConfiguredCowTypeKey(provider.level, Services.PLATFORM.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.BROWN);
        } else if (BovineRegistryUtil.getConfiguredCowTypeKey(provider.level, Services.PLATFORM.getMushroomCowTypeFromCow(provider)).equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.RED);
        }

        if (!this.provider.level.isClientSide) {
            this.syncMushroomType();
        }

        this.getMushroomCowType();
    }

    @Override
    public ResourceLocation getPreviousMushroomTypeKey() {
        return this.previousTypeId;
    }

    @Override
    public void setPreviousMushroomTypeKey(@Nullable ResourceLocation key) {
        this.previousTypeId = key;
        if (!this.provider.level.isClientSide) {
            this.syncMushroomType();
        }
    }

    @Override
    public void syncMushroomType() {
        if (this.typeId == null) return;
        BovinePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> provider), new SyncMushroomCowTypePacket(provider.getId(), this.typeId, this.previousTypeId));
    }
}
