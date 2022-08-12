package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BovineForgePacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Constants.resourceLocation("main"),
            () -> "0",
            "0"::equals,
            "0"::equals
    );

    public static void init() {
        int i = 0;
        INSTANCE.registerMessage(i++, CowTypeListPacket.class, CowTypeListPacket::encode, CowTypeListPacket::decode, getClientPacketHandler(CowTypeListPacket.Handler::handle));
    }

    public static <T> BiConsumer<T, Supplier<NetworkEvent.Context>> getClientPacketHandler(Consumer<T> handler) {
        return ((t, contextSupplier) -> {
            handler.accept(t);
            contextSupplier.get().setPacketHandled(true);
        });
    }
}
