package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Bee;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetComponentImpl implements FlowerCowTargetComponent, AutoSyncedComponent {
    public UUID moobloom = null;
    private final Bee provider;

    public FlowerCowTargetComponentImpl(Bee provider) {
        this.provider = provider;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (tag.contains("MoobloomTarget")) {
            setMoobloom(tag.getUUID("MoobloomTarget"));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (moobloom != null) {
            tag.putUUID("MoobloomTarget", moobloom);
        }
    }

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer player) {
        buf.writeBoolean(moobloom != null);
        if (moobloom != null) {
            buf.writeUUID(moobloom);
        }
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            this.moobloom = buf.readUUID();
        } else {
            this.moobloom = null;
        }
    }

    @Override
    public @Nullable UUID getMoobloom() {
        return moobloom;
    }

    @Override
    public void setMoobloom(@Nullable UUID moobloom) {
        this.moobloom = moobloom;
        BovineEntityComponents.FLOWER_COW_TARGET_COMPONENT.sync(provider);
    }
}