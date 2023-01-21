package net.merchantpug.bovinesandbuttercups.network;

import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.quiltmc.qsl.networking.api.client.ClientPlayNetworking;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BovinePacketsS2C {
    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(SyncDatapackContentsPacket.ID, createS2CHandler(SyncDatapackContentsPacket::decode, SyncDatapackContentsPacket::handle));
    }

    public static <T extends BovinePacket> ClientPlayNetworking.ChannelReceiver createS2CHandler(Function<FriendlyByteBuf, T> decode, BiConsumer<T, Minecraft> handler) {
        return (client, _handler, buf, responseSender) -> handler.accept(decode.apply(buf), client);
    }
}
