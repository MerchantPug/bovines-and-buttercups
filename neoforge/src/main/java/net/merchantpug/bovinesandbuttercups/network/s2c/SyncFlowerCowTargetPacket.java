package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.FlowerCowTargetCapability;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.DistExecutor;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Supplier;

public record SyncFlowerCowTargetPacket(int entityId, @Nullable UUID cowUUID) implements BovinePacketS2C {

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(cowUUID != null);
        if (cowUUID != null) {
            buf.writeUUID(cowUUID);
        }
    }

    public static SyncFlowerCowTargetPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        UUID cowUUID = null;
        if (buf.readBoolean()) {
            cowUUID = buf.readUUID();
        }
        return new SyncFlowerCowTargetPacket(entityId, cowUUID);
    }

    @Override
    public ResourceLocation getId() {
        throw new RuntimeException("BovinePacket#getFabricId is not meant to be used in Forge specific packets.");
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId());
            if (!(entity instanceof Bee)) {
                BovinesAndButtercups.LOG.warn("Attempted to get moobloom UUID from non Bee.");
                return;
            }
            entity.getCapability(FlowerCowTargetCapability.INSTANCE).ifPresent(cap -> cap.setMoobloom(cowUUID()));
        });
    }
}
