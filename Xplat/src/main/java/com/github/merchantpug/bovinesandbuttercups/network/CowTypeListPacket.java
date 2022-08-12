package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CowTypeListPacket implements IPacket {
    private final List<ICowTypeInstance> cowTypes;

    public CowTypeListPacket(List<ICowTypeInstance> cowTypes) {
        this.cowTypes = cowTypes;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cowTypes.size());
        for (ICowTypeInstance cowType : cowTypes) {
            buf.writeResourceLocation(cowType.getType().getId());
            cowType.getType().write(cowType, buf);
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return BovinePackets.COW_TYPE_LIST;
    }

    public @Nullable static CowTypeListPacket decode(FriendlyByteBuf buf) {
        int cowTypeSize = buf.readInt();
        List<ICowTypeInstance> cowTypes = new ArrayList<>();
        for (int i = 0; i < cowTypeSize; ++i) {
            ResourceLocation cowTypeResourceLocation = buf.readResourceLocation();
            ICowType cowType = CowTypeRegistry.getTypeFromId(cowTypeResourceLocation);
            ICowTypeInstance cowTypeInstance = cowType.read(buf);
            cowTypes.add(cowTypeInstance);
        }
        return new CowTypeListPacket(cowTypes);
    }

    public static class Handler {
        public static void handle(CowTypeListPacket packet) {
            Minecraft.getInstance().execute(() -> {
                CowTypeRegistry.reset();
                for (ICowTypeInstance cowTypeInstance : packet.cowTypes) {
                    CowTypeRegistry.register(cowTypeInstance);
                }
            });
        }
    }
}
