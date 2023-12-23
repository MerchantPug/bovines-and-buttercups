package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.registry.BovineAttachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public class LockdownEffectCapability implements ILockdownEffectAttachability {
    private final LivingEntity provider;

    public LockdownEffectCapability(LivingEntity entity) {
        this.provider = entity;
    }

    public void deserializeLegacyCap(CompoundTag tag) {
        if (!tag.contains("ForgeCaps", Tag.TAG_COMPOUND)) return;
        CompoundTag forgeCapsTag = tag.getCompound("ForgeCaps");
        if (!forgeCapsTag.contains(ILockdownEffectAttachability.ID.toString(), Tag.TAG_COMPOUND)) return;
        CompoundTag legacyTag = forgeCapsTag.getCompound(ILockdownEffectAttachability.ID.toString());
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

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects() {
        return provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).getLockdownMobEffects();
    }

    @Override
    public void addLockdownMobEffect(MobEffect effect, int duration) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).addLockdownMobEffect(effect, duration);
    }

    @Override
    public void removeLockdownMobEffect(MobEffect effect) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).removeLockdownMobEffect(effect);
    }

    @Override
    public void setLockdownMobEffects(Map<MobEffect, Integer> map) {
        provider.getData(BovineAttachments.LOCKDOWN_EFFECT.get()).setLockdownMobEffects(map);
    }

}