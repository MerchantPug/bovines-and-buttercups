package net.merchantpug.bovinesandbuttercups.network;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.MessageFunctions;
import net.neoforged.neoforge.network.simple.SimpleChannel;

import java.util.function.Consumer;

public class BovinePacketHandler {
    private static final String PROTOCOL_VERISON = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            BovinesAndButtercups.asResource("main"),
            () -> PROTOCOL_VERISON,
            PROTOCOL_VERISON::equals,
            PROTOCOL_VERISON::equals
    );

    public static void register() {
        int i = 0;
        INSTANCE.registerMessage(i++, SyncDatapackContentsPacket.class, SyncDatapackContentsPacket::encode, SyncDatapackContentsPacket::decode, BovinePacketHandler.createCommonS2CHandler(SyncDatapackContentsPacket::handle));
    }

    private static <MSG extends BovinePacketS2C> MessageFunctions.MessageConsumer<MSG> createCommonS2CHandler(Consumer<MSG> handler) {
        return (msg, ctx) -> {
            handler.accept(msg);
            ctx.setPacketHandled(true);
        };
    }
}
