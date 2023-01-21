package net.merchantpug.bovinesandbuttercups.network;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncFlowerCowTargetPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncLockdownEffectsPacket;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncMushroomCowTypePacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

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
        INSTANCE.registerMessage(i++, SyncDatapackContentsPacket.class, SyncDatapackContentsPacket::encode, SyncDatapackContentsPacket::decode, BovinePacketHandler::createCommonS2CHandler);
        INSTANCE.registerMessage(i++, SyncFlowerCowTargetPacket.class, SyncFlowerCowTargetPacket::encode, SyncFlowerCowTargetPacket::decode, SyncFlowerCowTargetPacket::handle);
        INSTANCE.registerMessage(i++, SyncLockdownEffectsPacket.class, SyncLockdownEffectsPacket::encode, SyncLockdownEffectsPacket::decode, SyncLockdownEffectsPacket::handle);
        INSTANCE.registerMessage(i++, SyncMushroomCowTypePacket.class, SyncMushroomCowTypePacket::encode, SyncMushroomCowTypePacket::decode, SyncMushroomCowTypePacket::handle);
    }

    public static <MSG extends BovinePacketClient> void createCommonS2CHandler(MSG msg, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> msg.handle(Minecraft.getInstance())));
        context.get().setPacketHandled(true);
    }
}
