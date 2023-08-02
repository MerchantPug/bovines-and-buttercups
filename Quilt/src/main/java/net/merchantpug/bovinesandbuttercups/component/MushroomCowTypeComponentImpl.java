package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.mixin.quilt.MushroomCowAccessor;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

public class MushroomCowTypeComponentImpl implements MushroomCowTypeComponent, AutoSyncedComponent {
    private ResourceLocation typeId;
    private ResourceLocation previousTypeId;
    private ConfiguredCowType<MushroomCowConfiguration, ?> type;
    private final MushroomCow provider;
    private boolean allowShearing = true;

    public MushroomCowTypeComponentImpl(MushroomCow provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return PlayerLookup.tracking(provider).contains(player);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING)) {
            this.setMushroomCowType(ResourceLocation.tryParse(tag.getString("Type")));
        }
        if (tag.contains("PreviousType", Tag.TAG_STRING)) {
            this.setPreviousMushroomCowTypeKey(ResourceLocation.tryParse(tag.getString("PreviousType")));
        }
        if (tag.contains("AllowShearing", Tag.TAG_BYTE)) {
            this.setAllowShearing(tag.getBoolean("AllowShearing"));
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
        tag.putBoolean("AllowShearing", this.allowShearing);
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType() {
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
    public void setMushroomCowType(ResourceLocation key) {
        this.typeId = key;

        if (key.equals(BovinesAndButtercups.asResource("brown_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.BROWN);
        } else if (key.equals(BovinesAndButtercups.asResource("red_mushroom"))) {
            ((MushroomCowAccessor)provider).bovinesandbuttercups$invokeSetVariant(MushroomCow.MushroomType.RED);
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
    public boolean shouldAllowShearing() {
        return this.allowShearing;
    }

    @Override
    public void setAllowShearing(boolean value) {
        this.allowShearing = value;
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer player) {
        buf.writeBoolean(this.typeId != null);
        if (this.typeId != null) {
            buf.writeResourceLocation(this.typeId);
        }
        buf.writeBoolean(this.allowShearing);
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            this.typeId = buf.readResourceLocation();
        }
        this.getMushroomCowType();
        this.allowShearing = buf.readBoolean();
    }
}
