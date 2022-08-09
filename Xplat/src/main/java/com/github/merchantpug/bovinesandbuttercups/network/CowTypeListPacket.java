package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class CowTypeListPacket implements IPacket {
    private final HashMap<ICowTypeInstance, ICowType> cowTypes;

    public CowTypeListPacket(HashMap<ICowTypeInstance, ICowType> cowTypes) {
        this.cowTypes = cowTypes;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cowTypes.size());
        for (Map.Entry<ICowTypeInstance, ICowType> cowType : cowTypes.entrySet()) {
            buf.writeResourceLocation(cowType.getValue().getId());
            cowType.getValue().write(cowType.getKey(), buf);
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return BovinePackets.COW_TYPE_LIST;
    }

    public @Nullable static CowTypeListPacket decode(FriendlyByteBuf buf) {
        try {
            int cowTypeSize = buf.readInt();
            HashMap<ICowTypeInstance, ICowType> cowTypes = new HashMap<>();
            for (int i = 0; i < cowTypeSize; i++) {
                ICowType cowType = CowTypeRegistry.getTypeFromId(buf.readResourceLocation());
                cowTypes.put(cowType.read(buf), cowType);
            }
            return new CowTypeListPacket(cowTypes);
        } catch (Exception e) {
            Constants.LOG.error(e.getMessage());
        }
        return null;
    }

    public static class Handler {
        public static void handle(CowTypeListPacket packet) {
            Minecraft.getInstance().execute(() -> {
                CowTypeRegistry.reset();
                for (ICowTypeInstance cowTypeInstance : packet.cowTypes.keySet()) {
                    CowTypeRegistry.register(cowTypeInstance);
                }
            });
        }
    }
}
