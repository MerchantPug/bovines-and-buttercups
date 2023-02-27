package net.merchantpug.bovinesandbuttercups.network;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncFlowerCowTargetPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncLockdownEffectsPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncMushroomCowTypePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

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
        INSTANCE.registerMessage(i++, SyncFlowerCowTargetPacket.class, SyncFlowerCowTargetPacket::encode, SyncFlowerCowTargetPacket::decode, SyncFlowerCowTargetPacket::handle);
        INSTANCE.registerMessage(i++, SyncLockdownEffectsPacket.class, SyncLockdownEffectsPacket::encode, SyncLockdownEffectsPacket::decode, SyncLockdownEffectsPacket::handle);
        INSTANCE.registerMessage(i++, SyncMushroomCowTypePacket.class, SyncMushroomCowTypePacket::encode, SyncMushroomCowTypePacket::decode, SyncMushroomCowTypePacket::handle);
    }

    private static <MSG extends BovinePacketS2C> BiConsumer<MSG, Supplier<NetworkEvent.Context>> createCommonS2CHandler(BiConsumer<MSG, Minecraft> handler) {
        return (msg, ctx) -> {
            handler.accept(msg, Minecraft.getInstance());
            ctx.get().setPacketHandled(true);
        };
    }
}
