package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.type.flower.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.entity.type.flower.FlowerCowTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class MoobloomTypeListPacket implements IPacket {
    private final ResourceLocation[] ids;
    private final FlowerCowType[] types;

    public MoobloomTypeListPacket(ResourceLocation[] ids, FlowerCowType[] types) {
        this.ids = ids;
        this.types = types;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(ids.length - 1);
        for (int i = 0; i < ids.length; i++) {
            if (types[i] != FlowerCowType.MISSING) {
                buf.writeResourceLocation(ids[i]);
                FlowerCowType.write(types[i], buf);
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return BovinePackets.MOOBLOOM_TYPE_LIST;
    }

    public @Nullable
    static MoobloomTypeListPacket decode(FriendlyByteBuf buf) {
        try {
            ResourceLocation[] ids = new ResourceLocation[buf.readInt()];
            FlowerCowType[] types = new FlowerCowType[ids.length];
            for (int i = 0; i < types.length; i++) {
                ids[i] = ResourceLocation.tryParse(buf.readUtf());
                types[i] = FlowerCowType.read(buf);
            }
            return new MoobloomTypeListPacket(ids, types);
        } catch (Exception e) {
            Constants.LOG.error(e.toString());
        }
        return null;
    }

    public static class Handler {
        public static void handle(MoobloomTypeListPacket packet) {
            Minecraft.getInstance().execute(() -> {
                FlowerCowTypeRegistry.reset();
                for (int i = 0; i < packet.ids.length; i++) {
                    FlowerCowTypeRegistry.register(packet.ids[i], packet.types[i]);
                }
            });
        }
    }
}
