package com.github.merchantpug.bovinesandbuttercups.network;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IPacket {
    default FriendlyByteBuf buf() {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        encode(buf);
        return buf;
    }

    void encode(FriendlyByteBuf buf);

    ResourceLocation getFabriQuiltId();
}
