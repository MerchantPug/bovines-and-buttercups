package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.flower.FlowerCowType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SyncMoobloomTypePacket implements IPacket {
    private final int moobloomId;
    private final FlowerCowType flowerCowType;

    public SyncMoobloomTypePacket(int moobloomId, FlowerCowType flowerCowType) {
        this.moobloomId = moobloomId;
        this.flowerCowType = flowerCowType;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(moobloomId);
        FlowerCowType.write(flowerCowType, buf);
    }

    @Override
    public ResourceLocation getFabricId() {
        return BovinePackets.SYNC_MOOBLOOM_TYPE;
    }

    public static SyncMoobloomTypePacket decode(FriendlyByteBuf buf) {
        return new SyncMoobloomTypePacket(buf.readInt(), FlowerCowType.read(buf));
    }

    public static class Handler {
        public static void handle(SyncMoobloomTypePacket packet) {
            Minecraft.getInstance().execute(() -> {
                if (Minecraft.getInstance().level == null) return;
                Entity moobloomEntity = Minecraft.getInstance().level.getEntity(packet.moobloomId);
                if (!(moobloomEntity instanceof FlowerCow)) {
                    Constants.LOG.warn("Received unknown moobloom");
                    return;
                }
                ((FlowerCow)moobloomEntity).type = packet.flowerCowType;
            });
        }
    }
}
