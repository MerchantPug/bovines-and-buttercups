package net.merchantpug.bovinesandbuttercups.network;

import net.minecraft.client.Minecraft;

public interface BovinePacketClient extends BovinePacket {
    void handle(Minecraft minecraft);
}
