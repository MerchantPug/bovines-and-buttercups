package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.FlowerCowTargetCapability;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Supplier;

public record SyncFlowerCowTargetPacket(int entityId, @Nullable UUID cowUUID) {

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

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().execute(() -> {
                Entity entity = Minecraft.getInstance().level.getEntity(entityId());
                if (!(entity instanceof Bee)) {
                    BovinesAndButtercups.LOG.warn("Attempted to get moobloom UUID from non Bee.");
                    return;
                }
                entity.getCapability(FlowerCowTargetCapability.INSTANCE).ifPresent(cap -> cap.setMoobloom(cowUUID()));
            });
        }));
        context.get().setPacketHandled(true);
    }
}
