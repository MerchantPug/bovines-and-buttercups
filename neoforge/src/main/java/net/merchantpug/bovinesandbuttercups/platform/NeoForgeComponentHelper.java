package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.services.IComponentHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineCapabilities;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AutoService(IComponentHelper.class)
public class NeoForgeComponentHelper implements IComponentHelper {

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowTypeFromCow(MushroomCow cow) {
        return Optional.ofNullable(cow.getCapability(BovineCapabilities.MOOSHROOM_TYPE)).map(MushroomCowTypeCapability::getMushroomCowType).orElse(BovineRegistryUtil.getConfiguredCowTypeFromKey(BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE.get()));
    }

    @Override
    public ResourceLocation getMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return Optional.ofNullable(cow.getCapability(BovineCapabilities.MOOSHROOM_TYPE)).map(MushroomCowTypeCapability::getMushroomCowTypeKey).orElse(BovinesAndButtercups.asResource("missing_mooshroom"));
    }

    @Override
    public Optional<ResourceLocation> getPreviousMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return Optional.ofNullable(cow.getCapability(BovineCapabilities.MOOSHROOM_TYPE)).map(MushroomCowTypeCapability::getPreviousMushroomTypeKey);
    }

    @Override
    public void setMushroomCowType(MushroomCow cow, ResourceLocation key) {
        Optional.ofNullable(cow.getCapability(BovineCapabilities.MOOSHROOM_TYPE)).ifPresent(capability -> capability.setMushroomType(key));
    }

    @Override
    public void setPreviousMushroomCowType(MushroomCow cow, @Nullable ResourceLocation key) {
        Optional.ofNullable(cow.getCapability(BovineCapabilities.MOOSHROOM_TYPE)).ifPresent(capability -> capability.setPreviousMushroomTypeKey(key));
    }

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects(LivingEntity entity) {
        return Optional.ofNullable(entity.getCapability(BovineCapabilities.LOCKDOWN_EFFECT)).map(LockdownEffectCapability::getLockdownMobEffects).orElse(LockdownEffectCapability.NO_EFFECTS);
    }

    @Override
    public void addLockdownMobEffect(LivingEntity entity, MobEffect effect, int duration) {
        Optional.ofNullable(entity.getCapability(BovineCapabilities.LOCKDOWN_EFFECT)).ifPresent(cap -> cap.addLockdownMobEffect(effect, duration));
    }

    @Override
    public void removeLockdownMobEffect(LivingEntity entity, MobEffect effect) {
        Optional.ofNullable(entity.getCapability(BovineCapabilities.LOCKDOWN_EFFECT)).ifPresent(cap -> cap.removeLockdownMobEffect(effect));
    }

    @Override
    public void setLockdownMobEffects(LivingEntity entity, Map<MobEffect, Integer> map) {
        Optional.ofNullable(entity.getCapability(BovineCapabilities.LOCKDOWN_EFFECT)).ifPresent(cap -> cap.setLockdownMobEffects(map));
    }

    @Override
    public void syncLockdownMobEffects(LivingEntity entity) {
        // Optional.ofNullable(entity.getCapability(BovineCapabilities.LOCKDOWN_EFFECT)).ifPresent(LockdownEffectCapability::sync);
    }

    @Override
    public Optional<UUID> getMoobloomTarget(Bee bee) {
        return Optional.ofNullable(bee.getCapability(BovineCapabilities.MOOBLOOM_TARGET)).map(FlowerCowTargetCapability::getMoobloom);
    }

    @Override
    public void setMoobloomTarget(Bee bee, @Nullable UUID uUID) {
        Optional.ofNullable(bee.getCapability(BovineCapabilities.MOOBLOOM_TARGET)).ifPresent(cap -> cap.setMoobloom(uUID));
    }
}
