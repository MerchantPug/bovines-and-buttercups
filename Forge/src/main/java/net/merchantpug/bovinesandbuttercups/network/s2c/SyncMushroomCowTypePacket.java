package net.merchantpug.bovinesandbuttercups.network.s2c;

import com.mojang.authlib.minecraft.client.MinecraftClient;
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

import java.util.function.Supplier;

public record SyncMushroomCowTypePacket(int entityId, ResourceLocation typeKey) {
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(typeKey);
    }

    public static SyncMushroomCowTypePacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation typeKey = buf.readResourceLocation();
        return new SyncMushroomCowTypePacket(entityId, typeKey);
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
                });
            });
        }));
        context.get().setPacketHandled(true);
    }
}
