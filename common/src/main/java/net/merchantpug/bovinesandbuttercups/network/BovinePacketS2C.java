package net.merchantpug.bovinesandbuttercups.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface BovinePacketS2C extends CustomPacketPayload {
    default FriendlyByteBuf toBuf() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        this.write(buf);
        return buf;
    }
    void handle();
}
