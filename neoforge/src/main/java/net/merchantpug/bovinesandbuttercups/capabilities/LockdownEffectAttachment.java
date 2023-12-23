package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class LockdownEffectAttachment implements ILockdownEffectAttachability, INBTSerializable<CompoundTag> {
    private Map<MobEffect, Integer> lockdownEffects = new HashMap<>();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        lockdownEffects.forEach((statusEffect, integer) -> {
            CompoundTag effectCompound = new CompoundTag();
            effectCompound.putString("id", BuiltInRegistries.MOB_EFFECT.getKey(statusEffect).toString());
            effectCompound.putInt("duration", integer);
            list.add(effectCompound);
        });
        tag.put("locked_effects", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (!tag.contains("locked_effects", Tag.TAG_LIST)) return;
        ListTag list = tag.getList("locked_effects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn(" NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("id", Tag.TAG_STRING) && compound.contains("duration", Tag.TAG_INT)) {
                addLockdownMobEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(compound.getString("id"))), compound.getInt("duration"));
            }
        }
    }

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects() {
        return lockdownEffects;
    }

    @Override
    public void addLockdownMobEffect(MobEffect effect, int duration) {
        lockdownEffects.put(effect, duration);
    }

    @Override
    public void removeLockdownMobEffect(MobEffect effect) {
        lockdownEffects.remove(effect);
    }

    @Override
    public void setLockdownMobEffects(Map<MobEffect, Integer> map) {
        lockdownEffects = map;
    }

}