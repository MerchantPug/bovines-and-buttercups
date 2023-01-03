package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.api.type.CowType;
import net.merchantpug.bovinesandbuttercups.capabilities.*;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.services.IComponentHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@AutoService(IComponentHelper.class)
public class ForgeComponentHelper implements IComponentHelper {

    @Override
    public ConfiguredCowType<MushroomCowConfiguration, CowType<MushroomCowConfiguration>> getMushroomCowTypeFromCow(MushroomCow cow) {
        return cow.getCapability(MushroomCowTypeCapability.INSTANCE).map(MushroomCowTypeCapabilityImpl::getMushroomCowType).orElse(BovineRegistryUtil.getConfiguredCowTypeFromKey(cow.getLevel(), BovinesAndButtercups.asResource("missing_mooshroom"), BovineCowTypes.MUSHROOM_COW_TYPE));
    }

    @Override
    public ResourceLocation getMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return cow.getCapability(MushroomCowTypeCapability.INSTANCE).map(MushroomCowTypeCapabilityImpl::getMushroomCowTypeKey).orElse(BovinesAndButtercups.asResource("missing_mooshroom"));
    }

    @Override
    public Optional<ResourceLocation> getPreviousMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return cow.getCapability(MushroomCowTypeCapability.INSTANCE).map(x -> Optional.ofNullable(x.getPreviousMushroomTypeKey())).orElseGet(Optional::empty);
    }

    @Override
    public void setMushroomCowType(MushroomCow cow, ResourceLocation key) {
        cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(capability -> capability.setMushroomType(key));
    }

    @Override
    public void setPreviousMushroomCowType(MushroomCow cow, @Nullable ResourceLocation key) {
        cow.getCapability(MushroomCowTypeCapability.INSTANCE).ifPresent(capability -> capability.setPreviousMushroomTypeKey(key));
    }

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects(LivingEntity entity) {
        if (entity.getCapability(LockdownEffectCapability.INSTANCE).resolve().isPresent()) {
            return entity.getCapability(LockdownEffectCapability.INSTANCE).resolve().get().getLockdownMobEffects();
        }
        return LockdownEffectCapability.NO_EFFECTS;
    }

    @Override
    public void addLockdownMobEffect(LivingEntity entity, MobEffect effect, int duration) {
        entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> cap.addLockdownMobEffect(effect, duration));
    }

    @Override
    public void removeLockdownMobEffect(LivingEntity entity, MobEffect effect) {
        entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> cap.removeLockdownMobEffect(effect));
    }

    @Override
    public void setLockdownMobEffects(LivingEntity entity, Map<MobEffect, Integer> map) {
        entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(cap -> cap.setLockdownMobEffects(map));
    }

    @Override
    public void syncLockdownMobEffects(LivingEntity entity) {
        entity.getCapability(LockdownEffectCapability.INSTANCE).ifPresent(LockdownEffectCapabilityImpl::sync);
    }

    @Override
    public Optional<UUID> getMoobloomTarget(Bee bee) {
        return bee.getCapability(FlowerCowTargetCapability.INSTANCE).map(x -> Optional.ofNullable(x.getMoobloom())).orElseGet(Optional::empty);
    }

    @Override
    public void setMoobloomTarget(Bee bee, @Nullable UUID uUID) {
        bee.getCapability(FlowerCowTargetCapability.INSTANCE).ifPresent(cap -> cap.setMoobloom(uUID));
    }
}
