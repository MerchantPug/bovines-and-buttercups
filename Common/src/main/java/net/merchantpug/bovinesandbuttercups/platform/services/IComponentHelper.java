package net.merchantpug.bovinesandbuttercups.platform.services;

import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IComponentHelper {

    ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowTypeFromCow(MushroomCow cow);

    ResourceLocation getMushroomCowTypeKeyFromCow(MushroomCow cow);

    Optional<ResourceLocation> getPreviousMushroomCowTypeKeyFromCow(MushroomCow cow);

    void setMushroomCowType(MushroomCow cow, ResourceLocation key);

    void setPreviousMushroomCowType(MushroomCow cow, ResourceLocation key);

    Map<MobEffect, Integer> getLockdownMobEffects(LivingEntity entity);

    void addLockdownMobEffect(LivingEntity entity, MobEffect effect, int duration);

    void removeLockdownMobEffect(LivingEntity entity, MobEffect effect);

    void setLockdownMobEffects(LivingEntity entity, Map<MobEffect, Integer> hashMap);

    void syncLockdownMobEffects(LivingEntity entity);

    Optional<UUID> getMoobloomTarget(Bee bee);

    void setMoobloomTarget(Bee bee, @Nullable UUID uUID);
}
