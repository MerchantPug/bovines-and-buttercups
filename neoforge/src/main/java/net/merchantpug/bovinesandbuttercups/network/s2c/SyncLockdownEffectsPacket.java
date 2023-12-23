package net.merchantpug.bovinesandbuttercups.network.s2c;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.capabilities.LockdownEffectCapability;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketS2C;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SyncLockdownEffectsPacket(int entityId, Map<MobEffect, Integer> effects) implements BovinePacketS2C {

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(effects.size());
        List<MobEffect> statusEffectList = effects.keySet().stream().toList();
        for (int i = 0; i < effects.size(); i++) {
            MobEffect effect = statusEffectList.get(i);
            buf.writeResourceLocation(BuiltInRegistries.MOB_EFFECT.getKey(effect));
            buf.writeInt(effects.get(effect));
        }
    }

    public static SyncLockdownEffectsPacket decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Map<MobEffect, Integer> lockedEffects = new HashMap<>();
        int lockdownEffectSize = buf.readInt();
        for (int i = 0; i < lockdownEffectSize; ++i) {
            lockedEffects.put(BuiltInRegistries.MOB_EFFECT.get(buf.readResourceLocation()), buf.readInt());
        }
        return new SyncLockdownEffectsPacket(entityId, lockedEffects);
    }

    @Override
    public ResourceLocation getId() {
        throw new RuntimeException("BovinePacket#getFabricId is not meant to be used in Forge specific packets.");
    }

    public void handle() {
        Minecraft.getInstance().execute(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(entityId());
            if (!(entity instanceof LivingEntity)) {
                BovinesAndButtercups.LOG.warn("Attempted to get lockdown effect from non LivingEntity.");
                return;
            }
            entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> cap.setLockdownMobEffects(effects()));
        });
    }
}
