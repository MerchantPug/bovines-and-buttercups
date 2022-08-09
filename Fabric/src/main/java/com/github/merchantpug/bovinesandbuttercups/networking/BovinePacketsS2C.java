package com.github.merchantpug.bovinesandbuttercups.networking;

import com.github.merchantpug.bovinesandbuttercups.network.BovinePackets;
import com.github.merchantpug.bovinesandbuttercups.network.CowTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.network.FlowerTypeListPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;
import java.util.function.Function;

public class BovinePacketsS2C {
    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(BovinePackets.COW_TYPE_LIST, getClientPacketHandler(CowTypeListPacket::decode, CowTypeListPacket.Handler::handle));
        ClientPlayNetworking.registerGlobalReceiver(BovinePackets.FLOWER_TYPE_LIST, getClientPacketHandler(FlowerTypeListPacket::decode, FlowerTypeListPacket.Handler::handle));
    }

    public static <T> ClientPlayNetworking.PlayChannelHandler getClientPacketHandler(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        return ((_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf)));
    }
}
