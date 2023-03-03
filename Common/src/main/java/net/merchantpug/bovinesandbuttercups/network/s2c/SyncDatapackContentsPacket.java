package net.merchantpug.bovinesandbuttercups.network.s2c;

import com.mojang.serialization.Codec;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record SyncDatapackContentsPacket(Map<ResourceLocation, ConfiguredCowType<?, ?>> configuredCowTypeMap,
                                         HashMap<ResourceLocation, FlowerType> flowerTypeMap,
                                         HashMap<ResourceLocation, MushroomType> mushroomTypeMap) implements BovinePacketS2C {
    public static final ResourceLocation ID = BovinesAndButtercups.asResource("sync_datapack_contents");

    @Override
    public void encode(FriendlyByteBuf buf) {
        Codec.unboundedMap(ResourceLocation.CODEC, ConfiguredCowType.getServerCodec()).encodeStart(NbtOps.INSTANCE, configuredCowTypeMap)
                .resultOrPartial(msg -> {
                    buf.writeBoolean(false);
                    BovinesAndButtercups.LOG.error("Failed to encode configured cow types. {}", msg);
                })
                .ifPresent(tag -> {
                    buf.writeBoolean(true);
                    buf.writeNbt((CompoundTag) tag);
                });

        Codec.unboundedMap(ResourceLocation.CODEC, FlowerType.CODEC.codec()).encodeStart(NbtOps.INSTANCE, flowerTypeMap)
                .resultOrPartial(msg -> {
                    buf.writeBoolean(false);
                    BovinesAndButtercups.LOG.error("Failed to encode flower types. {}", msg);
                })
                .ifPresent(tag -> {
                    buf.writeBoolean(true);
                    buf.writeNbt((CompoundTag) tag);
                });

        Codec.unboundedMap(ResourceLocation.CODEC, MushroomType.CODEC.codec()).encodeStart(NbtOps.INSTANCE, mushroomTypeMap)
                .resultOrPartial(msg -> {
                    buf.writeBoolean(false);
                    BovinesAndButtercups.LOG.error("Failed to encode mushroom types. {}", msg);
                })
                .ifPresent(tag -> {
                    buf.writeBoolean(true);
                    buf.writeNbt((CompoundTag) tag);
                });
        }

    public static SyncDatapackContentsPacket decode(FriendlyByteBuf buf) {
        HashMap<ResourceLocation, ConfiguredCowType<?, ?>> configuredCowTypeMap = new HashMap<>();
        if (buf.readBoolean()) {
            CompoundTag compoundTag = buf.readNbt();
            Codec.unboundedMap(ResourceLocation.CODEC, ConfiguredCowType.getClientCodec()).parse(NbtOps.INSTANCE, compoundTag)
                    .resultOrPartial(msg -> BovinesAndButtercups.LOG.error("Failed to decode configured cow types. {}", msg))
                    .ifPresent(configuredCowTypeMap::putAll);
        }

        HashMap<ResourceLocation, FlowerType> flowerTypeMap = new HashMap<>();
        if (buf.readBoolean()) {
            CompoundTag compoundTag = buf.readNbt();
            Codec.unboundedMap(ResourceLocation.CODEC, FlowerType.CODEC.codec()).parse(NbtOps.INSTANCE, compoundTag)
                    .resultOrPartial(msg -> BovinesAndButtercups.LOG.error("Failed to decode flower types. {}", msg))
                    .ifPresent(flowerTypeMap::putAll);
        }

        HashMap<ResourceLocation, MushroomType> mushroomTypeMap = new HashMap<>();
        if (buf.readBoolean()) {
            CompoundTag compoundTag = buf.readNbt();
            Codec.unboundedMap(ResourceLocation.CODEC, MushroomType.CODEC.codec()).parse(NbtOps.INSTANCE, compoundTag)
                    .resultOrPartial(msg -> BovinesAndButtercups.LOG.error("Failed to decode mushroom types. {}", msg))
                    .ifPresent(mushroomTypeMap::putAll);
        }
        return new SyncDatapackContentsPacket(configuredCowTypeMap, flowerTypeMap, mushroomTypeMap);
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            ConfiguredCowTypeRegistry.clear();
            configuredCowTypeMap().forEach(ConfiguredCowTypeRegistry::register);
            FlowerTypeRegistry.clear();
            flowerTypeMap().forEach(FlowerTypeRegistry::register);
            MushroomTypeRegistry.clear();
            mushroomTypeMap().forEach(MushroomTypeRegistry::register);
        });
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
