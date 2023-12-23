package net.merchantpug.bovinesandbuttercups.capabilities;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketHandler;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncLockdownEffectsPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LockdownEffectCapabilityImpl implements LockdownEffectCapability, ICapabilityProvider {
    private Map<MobEffect, Integer> lockdownEffects = new HashMap<>();
    LivingEntity provider;
    private final LazyOptional<LockdownEffectCapabilityImpl> lazyOptional = LazyOptional.of(() -> this);

    public LockdownEffectCapabilityImpl(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        lockdownEffects.forEach((statusEffect, integer) -> {
            CompoundTag effectCompound = new CompoundTag();
            effectCompound.putString("Id", BuiltInRegistries.MOB_EFFECT.getKey(statusEffect).toString());
            effectCompound.putInt("Duration", integer);
            list.add(effectCompound);
        });
        tag.put("LockedEffects", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (!tag.contains("LockedEffects", Tag.TAG_LIST)) return;
        ListTag list = tag.getList("LockedEffects", Tag.TAG_COMPOUND);
        for (Tag nbtElement : list) {
            if (!(nbtElement instanceof CompoundTag compound)) {
                BovinesAndButtercups.LOG.warn("LockedEffects NBT is not a CompoundTag.");
                continue;
            }
            if (compound.contains("Id", Tag.TAG_STRING) && compound.contains("Duration", Tag.TAG_INT)) {
                addLockdownMobEffect(BuiltInRegistries.MOB_EFFECT.get(ResourceLocation.tryParse(compound.getString("Id"))), compound.getInt("Duration"));
            }
        }
        sync();
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
    public void sync() {
        if (provider.level().isClientSide) return;
        BovinePacketHandler.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> provider), new SyncLockdownEffectsPacket(provider.getId(), this.lockdownEffects));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return LockdownEffectCapability.INSTANCE.orEmpty(cap, this.lazyOptional);
    }
}