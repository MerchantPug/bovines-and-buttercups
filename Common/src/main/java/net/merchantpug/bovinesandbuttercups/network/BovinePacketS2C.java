package net.merchantpug.bovinesandbuttercups.network;

import net.minecraft.client.Minecraft;

public interface BovinePacketS2C extends BovinePacket {
    void handle(Minecraft minecraft);
}
