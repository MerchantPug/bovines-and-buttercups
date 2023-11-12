package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class LockdownEffectComponentImpl implements LockdownEffectComponent, AutoSyncedComponent {
    public Map<MobEffect, Integer> lockdownEffects = new HashMap<>();
    private final LivingEntity provider;

    public LockdownEffectComponentImpl(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public boolean shouldSyncWith(ServerPlayer player) {
        return provider == player || PlayerLookup.tracking(provider).contains(player);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        if (!tag.contains("LockedEffects", Tag.TAG_LIST)) return;
        ListTag list = tag.getList("LockedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn("LockedEffects NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_STRING) && compound.contains("Duration", Tag.TAG_INT)) {
                lockdownEffects.put(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(compound.getString("Id"))), compound.getInt("Duration"));
            }
        }

        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.sync(provider);
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        if (this.lockdownEffects.isEmpty()) return;
        ListTag list = new ListTag();
        lockdownEffects.forEach(((statusEffect, integer) -> {
            CompoundTag effectCompound = new CompoundTag();
            effectCompound.putString("Id", BuiltInRegistries.MOB_EFFECT.getKey(statusEffect).toString());
            effectCompound.putInt("Duration", integer);
            list.add(effectCompound);
        }));
        tag.put("LockedEffects", list);
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

    @Override
    public void writeSyncPacket(FriendlyByteBuf buf, ServerPlayer player) {
        buf.writeInt(lockdownEffects.size());
        for (Map.Entry<MobEffect, Integer> entry : lockdownEffects.entrySet()) {
            buf.writeResourceLocation(BuiltInRegistries.MOB_EFFECT.getKey(entry.getKey()));
            buf.writeInt(entry.getValue());
        }
    }

    @Override
    public void applySyncPacket(FriendlyByteBuf buf) {
        HashMap<MobEffect, Integer> hashMap = new HashMap<>();
        int lockdownEffectSize = buf.readInt();
        for (int i = 0; i < lockdownEffectSize; ++i) {
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(buf.readResourceLocation());
            int duration = buf.readInt();
            hashMap.put(effect, duration);
        }
        this.lockdownEffects = hashMap;
    }
}