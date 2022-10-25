package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public record SyncLockdownEffectsPacket(int entityId, Map<MobEffect, Integer> effects) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(effects.size());
        List<MobEffect> statusEffectList = effects.keySet().stream().toList();
        for (int i = 0; i < effects.size(); i++) {
            MobEffect effect = statusEffectList.get(i);
            buf.writeResourceLocation(ForgeRegistries.MOB_EFFECTS.getKey(effect));
            buf.writeInt(effects.get(effect));
        }
    }

    public static SyncLockdownEffectsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Map<MobEffect, Integer> lockedEffects = new HashMap<>();
        int lockdownEffectSize = buf.readInt();
        for (int i = 0; i < lockdownEffectSize; ++i) {
            lockedEffects.put(ForgeRegistries.MOB_EFFECTS.getValue(buf.readResourceLocation()), buf.readInt());
        }
        return new SyncLockdownEffectsPacket(entityId, lockedEffects);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            Minecraft.getInstance().execute(() -> {
                Entity entity = Minecraft.getInstance().level.getEntity(entityId());
                if (!(entity instanceof LivingEntity)) {
                    BovinesAndButtercups.LOG.warn("Attempted to get lockdown effect from non LivingEntity.");
                    return;
                }
                entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> cap.setLockdownMobEffects(effects()));
            });
        }));
        context.get().setPacketHandled(true);
    }
}