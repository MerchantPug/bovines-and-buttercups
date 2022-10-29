package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.MushroomCowTypeCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record SyncMushroomCowTypePacket(int entityId, ResourceLocation typeKey, @Nullable ResourceLocation previousTypeKey) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(typeKey);
        buf.writeBoolean(previousTypeKey != null);
        if (previousTypeKey != null) {
            buf.writeResourceLocation(previousTypeKey);
        }
    }

    public static SyncMushroomCowTypePacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation typeKey = buf.readResourceLocation();
        ResourceLocation previousTypeKey = null;
        if (buf.readBoolean()) {
            previousTypeKey = buf.readResourceLocation();
        }
        return new SyncMushroomCowTypePacket(entityId, typeKey, previousTypeKey);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().execute(() -> {
                Entity entity = Minecraft.getInstance().level.getEntity(entityId());
                if (!(entity instanceof MushroomCow mushroomCow)) {
                    BovinesAndButtercups.LOG.warn("Could not find MushroomCow to set type of.");
                    return;
                }
                mushroomCow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(capability -> {
                    capability.setMushroomType(typeKey());
                    capability.setPreviousMushroomTypeKey(previousTypeKey());
                });
            });
        }));
        context.get().setPacketHandled(true);
    }
}
