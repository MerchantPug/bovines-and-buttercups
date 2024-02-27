package net.merchantpug.bovinesandbuttercups.attachment.api;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncLockdownEffectAttachmentPacket;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachmentTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class LockdownEffectApi {
    private final LivingEntity provider;

    public LockdownEffectApi(LivingEntity entity) {
        this.provider = entity;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("cardinal_components", Tag.TAG_COMPOUND)) return;
        CompoundTag ccaTag = tag.getCompound("cardinal_components");
        if (!ccaTag.contains(LockdownEffectAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = ccaTag.getCompound(LockdownEffectAttachment.ID.toString());
        ListTag list = legacyTag.getList("LockedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn("LockedEffects NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_BYTE) && compound.contains("Duration", Tag.TAG_INT)) {
                addLockdownMobEffect(BuiltInRegistries.MOB_EFFECT.byId(compound.getByte("Id")), compound.getInt("Duration"));
            }
            if (compound.contains("Id", Tag.TAG_STRING) && compound.contains("Duration", Tag.TAG_INT)) {
                addLockdownMobEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(compound.getString("Id"))), compound.getInt("Duration"));
            }
        }
    }

    public Map<MobEffect, Integer> getLockdownMobEffects() {
        return this.provider.getAttachedOrCreate(BovineAttachmentTypes.LOCKDOWN_EFFECT, LockdownEffectAttachment::new).getLockdownMobEffects();
    }

    public void addLockdownMobEffect(MobEffect effect, int duration) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.LOCKDOWN_EFFECT, LockdownEffectAttachment::new).addLockdownMobEffect(effect, duration);
    }

    public void removeLockdownMobEffect(MobEffect effect) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.LOCKDOWN_EFFECT, LockdownEffectAttachment::new).removeLockdownMobEffect(effect);
    }

    public void setLockdownMobEffects(Map<MobEffect, Integer> map) {
        this.provider.getAttachedOrCreate(BovineAttachmentTypes.LOCKDOWN_EFFECT, LockdownEffectAttachment::new).setLockdownMobEffects(map);
    }

    public void sync() {
        if (!(this.provider instanceof ServerPlayer serverPlayer)) return;
        SyncLockdownEffectAttachmentPacket packet = new SyncLockdownEffectAttachmentPacket(this.provider.getId(), this.provider.getAttachedOrCreate(BovineAttachmentTypes.LOCKDOWN_EFFECT));
        ServerPlayNetworking.send(serverPlayer, packet.id(), packet.toBuf());
    }

}