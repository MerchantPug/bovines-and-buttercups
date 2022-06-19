package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommon;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BovineForgePacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            BovinesAndButtercupsCommon.resourceLocation("main"),
            () -> "0",
            "0"::equals,
            "0"::equals
    );

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(i++, MoobloomTypeListPacket.class, MoobloomTypeListPacket::encode, MoobloomTypeListPacket::decode, getClientPacketHandler(MoobloomTypeListPacket.Handler::handle));
        INSTANCE.registerMessage(i++, SyncMoobloomTypePacket.class, SyncMoobloomTypePacket::encode, SyncMoobloomTypePacket::decode, getClientPacketHandler(SyncMoobloomTypePacket.Handler::handle));
    }

    public static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> getClientPacketHandler(Consumer<T> handler) {
        return ((t, contextSupplier) -> {
            handler.accept(t);
            contextSupplier.get().setPacketHandled(true);
        });
    }
}
