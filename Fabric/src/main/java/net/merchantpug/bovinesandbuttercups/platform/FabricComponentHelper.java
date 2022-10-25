package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.services.IComponentHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.MushroomCow;

import java.util.Map;
import java.util.Optional;

@AutoService(IComponentHelper.class)
public class FabricComponentHelper implements IComponentHelper {
    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowTypeFromCow(MushroomCow cow) {
        return BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(cow).getMushroomCowType();
    }

    @Override
    public ResourceLocation getMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(cow).getMushroomCowTypeKey();
    }

    @Override
    public Optional<ResourceLocation> getPreviousMushroomCowTypeKeyFromCow(MushroomCow cow) {
        return Optional.ofNullable(BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(cow).getPreviousMushroomCowTypeKey());
    }

    @Override
    public void setMushroomCowType(MushroomCow cow, ResourceLocation key) {
        BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(cow).setMushroomCowType(key);
    }

    @Override
    public void setPreviousMushroomCowType(MushroomCow cow, ResourceLocation key) {
        BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(cow).setPreviousMushroomCowTypeKey(key);
    }

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects(LivingEntity entity) {
        return BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(entity).getLockdownMobEffects();
    }

    @Override
    public void addLockdownMobEffect(LivingEntity entity, MobEffect effect, int duration) {
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(entity).addLockdownMobEffect(effect, duration);
    }

    @Override
    public void removeLockdownMobEffect(LivingEntity entity, MobEffect effect) {
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(entity).removeLockdownMobEffect(effect);
    }

    @Override
    public void setLockdownMobEffects(LivingEntity entity, Map<MobEffect, Integer> map) {
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.get(entity).setLockdownMobEffects(map);
    }

    @Override
    public void syncLockdownMobEffects(LivingEntity entity) {
        BovineEntityComponents.LOCKDOWN_EFFECT_COMPONENT.sync(entity);
    }
}