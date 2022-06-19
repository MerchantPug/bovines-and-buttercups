package com.github.merchantpug.bovinesandbuttercups.networking;

import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.network.BovinePackets;
import com.github.merchantpug.bovinesandbuttercups.network.MoobloomTypeListPacket;
import com.github.merchantpug.bovinesandbuttercups.network.SyncMoobloomTypePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;
import java.util.function.Function;

public class BovinePacketsS2C {
    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(BovinePackets.MOOBLOOM_TYPE_LIST, getClientPacketHandler(MoobloomTypeListPacket::decode, MoobloomTypeListPacket.Handler::handle));
        ClientPlayNetworking.registerGlobalReceiver(BovinePackets.SYNC_MOOBLOOM_TYPE, getClientPacketHandler(SyncMoobloomTypePacket::decode, SyncMoobloomTypePacket.Handler::handle));
    }

    public static <T> ClientPlayNetworking.PlayChannelHandler getClientPacketHandler(Function<FriendlyByteBuf, T> decoder, Consumer<T> handler) {
        return ((_client, _handler, buf, _responseSender) -> handler.accept(decoder.apply(buf)));
    }
}
