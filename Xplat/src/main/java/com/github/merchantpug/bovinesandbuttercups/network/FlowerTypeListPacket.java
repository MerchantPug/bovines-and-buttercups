package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.api.ICowTypeInstance;
import com.github.merchantpug.bovinesandbuttercups.data.CowTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class FlowerTypeListPacket implements IPacket {
    private final List<FlowerType> flowerTypes;

    public FlowerTypeListPacket(List<FlowerType> cowTypes) {
        this.flowerTypes = cowTypes;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(flowerTypes.size() - 1);
        for (FlowerType flowerType : flowerTypes) {
            if (flowerType != FlowerType.MISSING) {
                FlowerType.write(flowerType, buf);
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return BovinePackets.FLOWER_TYPE_LIST;
    }

    public @Nullable static FlowerTypeListPacket decode(FriendlyByteBuf buf) {
        try {
            int flowerTypeSize = buf.readInt();
            List<FlowerType> flowerTypes = new ArrayList<>();
            for (int i = 0; i < flowerTypeSize; i++) {
                flowerTypes.add(FlowerType.read(buf));
            }
            return new FlowerTypeListPacket(flowerTypes);
        } catch (Exception e) {
            Constants.LOG.error(e.getMessage());
        }
        return null;
    }

    public static class Handler {
        public static void handle(FlowerTypeListPacket packet) {
            Minecraft.getInstance().execute(() -> {
                FlowerTypeRegistry.reset();
                for (FlowerType flowerType : packet.flowerTypes) {
                    FlowerTypeRegistry.register(flowerType);
                }
            });
        }
    }
}
