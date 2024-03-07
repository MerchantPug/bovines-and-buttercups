package net.merchantpug.bovinesandbuttercups.attachment.capability;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncLockdownEffectAttachmentPacket;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

public class LockdownEffectCapability {
    private final LivingEntity provider;

    public LockdownEffectCapability(LivingEntity entity) {
        this.provider = entity;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(LockdownEffectAttachment.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(LockdownEffectAttachment.ID.toString());
        ListTag list = legacyTag.getList("LockedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn("LockedEffects NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_STRING) && compound.contains("Duration", Tag.TAG_INT)) {
                addLockdownMobEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(compound.getString("Id"))), compound.getInt("Duration"));
            }
        }
    }

    public Map<MobEffect, Integer> getLockdownMobEffects() {
        return provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).get().getLockdownMobEffects();
    }

    public void addLockdownMobEffect(MobEffect effect, int duration) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).get().addLockdownMobEffect(effect, duration);
    }

    public void removeLockdownMobEffect(MobEffect effect) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).get().removeLockdownMobEffect(effect);
    }

    public void setLockdownMobEffects(Map<MobEffect, Integer> map) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).get().setLockdownMobEffects(map);
    }

    public void sync() {
        if (!(this.provider instanceof ServerPlayer serverPlayer)) return;
        SyncLockdownEffectAttachmentPacket packet = new SyncLockdownEffectAttachmentPacket(this.provider.getId(), this.provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).get());
        PacketDistributor.PLAYER.with(serverPlayer).send(packet);
    }

}