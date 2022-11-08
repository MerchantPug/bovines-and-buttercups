package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.fabriclike.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.MushroomCow;

import javax.annotation.Nullable;

public class MushroomCowTypeComponentImpl implements MushroomCowTypeComponent, AutoSyncedComponent {
    private ResourceLocation typeId;
    private ResourceLocation previousTypeId;
    private ConfiguredCowType<MushroomCowConfiguration, ?> type;
    private final MushroomCow provider;

    public MushroomCowTypeComponentImpl(MushroomCow provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            this.setMushroomCowType(ResourceLocation.tryParse(tag.getString("Type")));
        }
        if (tag.contains("PreviousType", Tag.TAG_STRING)) {
            this.setPreviousMushroomCowTypeKey(ResourceLocation.tryParse(tag.getString("PreviousType")));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (this.typeId != null) {
            tag.putString("Type", this.typeId.toString());
        }
        if (this.previousTypeId != null) {
            tag.putString("PreviousType", this.previousTypeId.toString());
        }
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId) && this.type != null && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE).getConfiguration()) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId)) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE);
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
    public void setMushroomCowType(ResourceLocation key) {
        this.typeId = key;

        if (key.equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.BROWN);
        } else if (key.equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetMushroomType(MushroomCow.MushroomType.RED);
        }

        BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.sync(provider);

        this.getMushroomCowType();
    }

    @Override
    @Nullable
    public ResourceLocation getPreviousMushroomCowTypeKey() {
        return this.previousTypeId;
    }

    @Override
    public void setPreviousMushroomCowTypeKey(@Nullable ResourceLocation key) {
        this.previousTypeId = key;
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer player) {
        buf.writeBoolean(this.typeId != null);
        if (this.typeId != null) {
            buf.writeResourceLocation(this.typeId);
        }
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            this.typeId = buf.readResourceLocation();
        }
        this.getMushroomCowType();
    }
}
