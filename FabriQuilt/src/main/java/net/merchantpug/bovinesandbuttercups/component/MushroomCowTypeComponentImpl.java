package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowTypeRegistryUtil;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
        if (tag.contains("type", Tag.TAG_STRING)) {
            this.typeId = ResourceLocation.tryParse(tag.getString("type"));
        } else if (this.provider.getMushroomType() == MushroomCow.MushroomType.BROWN) {
            this.typeId = BovinesAndButtercups.asResource("brown_mushroom");
        } else if (this.provider.getMushroomType() == MushroomCow.MushroomType.RED) {
            this.typeId = BovinesAndButtercups.asResource("red_mushroom");
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (this.typeId == null) {
            if (this.provider.getMushroomType() == MushroomCow.MushroomType.BROWN) {
                this.typeId = BovinesAndButtercups.asResource("brown_mushroom");
            } else if (this.provider.getMushroomType() == MushroomCow.MushroomType.RED) {
                this.typeId = BovinesAndButtercups.asResource("red_mushroom");
            }
        }

        tag.putString("type", this.typeId.toString());
    }

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowType() {
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

    @Override
    public void setMushroomCowType(ResourceLocation key) {
        this.typeId = key;
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
