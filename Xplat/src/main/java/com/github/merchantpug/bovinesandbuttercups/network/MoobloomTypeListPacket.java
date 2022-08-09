package com.github.merchantpug.bovinesandbuttercups.network;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow.FlowerCowTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class MoobloomTypeListPacket implements IPacket {
    private final ResourceLocation[] moobloomKeys;
    private final FlowerCowType[] moobloomTypes;
    private final ResourceLocation[] flowerKeys;
    private final FlowerType[] flowerTypes;

    public MoobloomTypeListPacket(ResourceLocation[] moobloomKeys, FlowerCowType[] moobloomTypes, ResourceLocation[] flowerKeys, FlowerType[] flowerTypes) {
        this.moobloomKeys = moobloomKeys;
        this.moobloomTypes = moobloomTypes;
        this.flowerKeys = flowerKeys;
        this.flowerTypes = flowerTypes;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(moobloomKeys.length - 1);
        for (int i = 0; i < moobloomKeys.length; i++) {
            if (moobloomTypes[i] != FlowerCowType.MISSING) {
                buf.writeResourceLocation(moobloomKeys[i]);
                FlowerCowType.write(moobloomTypes[i], buf);
            }
        }

        buf.writeInt(flowerKeys.length - 1);
        for (int i = 0; i < flowerKeys.length; i++) {
            if (flowerTypes[i] != FlowerType.MISSING) {
                buf.writeResourceLocation(flowerKeys[i]);
                FlowerType.write(flowerTypes[i], buf);
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
            ResourceLocation[] moobloomKeys = new ResourceLocation[buf.readInt()];
            FlowerCowType[] moobloomTypes = new FlowerCowType[moobloomKeys.length];
            for (int i = 0; i < moobloomTypes.length; i++) {
                moobloomKeys[i] = ResourceLocation.tryParse(buf.readUtf());
                moobloomTypes[i] = FlowerCowType.read(buf);
            }

            ResourceLocation[] flowerKeys = new ResourceLocation[buf.readInt()];
            FlowerType[] flowerTypes = new FlowerType[flowerKeys.length];
            for (int i = 0; i < flowerTypes.length; i++) {
                flowerKeys[i] = ResourceLocation.tryParse(buf.readUtf());
                flowerTypes[i] = FlowerType.read(buf);
            }

            return new MoobloomTypeListPacket(moobloomKeys, moobloomTypes, flowerKeys, flowerTypes);
        } catch (Exception e) {
            Constants.LOG.error(e.toString());
        }
        return null;
    }

    public static class Handler {
        public static void handle(MoobloomTypeListPacket packet) {
            Minecraft.getInstance().execute(() -> {
                FlowerCowTypeRegistry.reset();
                FlowerTypeRegistry.reset();
                for (int i = 0; i < packet.moobloomKeys.length; i++) {
                    FlowerCowTypeRegistry.register(packet.moobloomKeys[i], packet.moobloomTypes[i]);
                }
                for (int i = 0; i < packet.flowerKeys.length; i++) {
                    FlowerTypeRegistry.register(packet.flowerKeys[i], packet.flowerTypes[i]);
                }
            });
        }
    }
}
