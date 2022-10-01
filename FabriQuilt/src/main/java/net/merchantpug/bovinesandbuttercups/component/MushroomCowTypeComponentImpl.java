package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.MushroomCow;

public class MushroomCowTypeComponentImpl implements MushroomCowTypeComponent, AutoSyncedComponent {
    ResourceLocation typeId;
    ConfiguredCowType<MushroomCowConfiguration, ?> type;
    private final MushroomCow provider;

    public MushroomCowTypeComponentImpl(MushroomCow provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        this.setMushroomCowType(ResourceLocation.tryParse(tag.getString("type")));
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (this.typeId != null) {
            tag.putString("type", this.typeId.toString());
        }
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType() {
        try {
            if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId) && this.type.getConfiguration() != BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get()).getConfiguration()) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get());
            } else if (this.type != null) {
                return this.type;
            } else if (BovineRegistryUtil.isConfiguredCowTypeInRegistry(provider.getLevel(), typeId)) {
                return BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), typeId, BovineCowTypes.MUSHROOM_COW_TYPE.get());
            }
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
            return this.type;
        } catch (Exception e) {
            this.type = BovineRegistryUtil.getConfiguredCowTypeFromKey(provider.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get());
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
        BovineEntityComponentInitializer.MUSHROOM_COW_TYPE_COMPONENT.sync(provider);
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
    }
}
