package net.merchantpug.bovinesandbuttercups.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Consumer;
import java.util.function.Function;

public class BovinePacketsS2C {
    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SyncDatapackContentsPacket.ID, createS2CHandler(SyncDatapackContentsPacket::read, SyncDatapackContentsPacket::handle));
    }

    public static <T extends CustomPacketPayload> ClientPlayNetworking.PlayChannelHandler createS2CHandler(Function<FriendlyByteBuf, T> decode, Consumer<T> handler) {
        return (client, _handler, buf, responseSender) -> handler.accept(decode.apply(buf));
    }
}
