package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.MushroomCowTypeCapability;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

public record SyncMushroomCowTypePacket(int entityId, ResourceLocation typeKey, @Nullable ResourceLocation previousTypeKey, boolean allowShearing) implements BovinePacketS2C {
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(typeKey);
        buf.writeBoolean(previousTypeKey != null);
        if (previousTypeKey != null) {
            buf.writeResourceLocation(previousTypeKey);
        }
        buf.writeBoolean(allowShearing);
    }

    public static SyncMushroomCowTypePacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        ResourceLocation typeKey = buf.readResourceLocation();
        ResourceLocation previousTypeKey = null;
        if (buf.readBoolean()) {
            previousTypeKey = buf.readResourceLocation();
        }
        boolean allowShearing = buf.readBoolean();
        return new SyncMushroomCowTypePacket(entityId, typeKey, previousTypeKey, allowShearing);
    }

    @Override
    public ResourceLocation getId() {
        throw new RuntimeException("BovinePacket#getFabricId is not meant to be used in Forge specific packets.");
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId());
            if (!(entity instanceof MushroomCow mushroomCow)) {
                BovinesAndButtercups.LOG.warn("Could not find MushroomCow to set type of.");
                return;
            }
            mushroomCow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(capability -> {
                capability.setMushroomType(typeKey());
                capability.setPreviousMushroomTypeKey(previousTypeKey());
                capability.setAllowShearing(allowShearing());
            });
        });
    }
}
