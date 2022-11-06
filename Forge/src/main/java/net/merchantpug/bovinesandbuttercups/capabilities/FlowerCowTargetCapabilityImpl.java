package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncFlowerCowTargetPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FlowerCowTargetCapabilityImpl implements FlowerCowTargetCapability {
    public UUID moobloom = null;
    Bee provider;

    public FlowerCowTargetCapabilityImpl(Bee provider) {
        this.provider = provider;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (moobloom != null) {
            tag.putUUID("MoobloomTarget", moobloom);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("MoobloomTarget")) {
            setMoobloom(tag.getUUID("MoobloomTarget"));
        }
    }

    @Override
    public void sync() {
        if (provider.level.isClientSide) return;
        BovinePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> provider), new SyncFlowerCowTargetPacket(provider.getId(), this.moobloom));
    }

    @Override
    public @Nullable UUID getMoobloom() {
        return moobloom;
    }

    @Override
    public void setMoobloom(@Nullable UUID moobloom) {
        this.moobloom = moobloom;
        this.sync();
    }
}
