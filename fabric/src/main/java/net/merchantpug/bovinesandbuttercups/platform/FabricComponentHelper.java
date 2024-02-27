package net.merchantpug.bovinesandbuttercups.platform;

import com.google.auto.service.AutoService;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.attachment.LockdownEffectAttachment;
import net.merchantpug.bovinesandbuttercups.attachment.api.FlowerCowTargetApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.LockdownEffectApi;
import net.merchantpug.bovinesandbuttercups.attachment.api.MushroomCowTypeApi;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.services.IComponentHelper;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityApis;
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
public class FabricComponentHelper implements IComponentHelper {
    @Override
    public ConfiguredCowType<MushroomCowConfiguration, ?> getMushroomCowTypeFromCow(MushroomCow cow) {
        MushroomCowTypeApi api = BovineEntityApis.MOOSHROOM_TYPE.find(cow, null);
        if (api == null) {
            return null;
        }
        return api.getType();
    }

    @Override
    public ResourceLocation getMushroomCowTypeKeyFromCow(MushroomCow cow) {
        MushroomCowTypeApi api = BovineEntityApis.MOOSHROOM_TYPE.find(cow, null);
        if (api == null) {
            return null;
        }
        return api.getTypeKey().orElse(null);
    }

    @Override
    public Optional<ResourceLocation> getPreviousMushroomCowTypeKeyFromCow(MushroomCow cow) {
        MushroomCowTypeApi api = BovineEntityApis.MOOSHROOM_TYPE.find(cow, null);
        if (api == null) {
            return Optional.empty();
        }
        return api.getPreviousTypeKey();
    }

    @Override
    public void setMushroomCowType(MushroomCow cow, ResourceLocation key) {
        MushroomCowTypeApi api = BovineEntityApis.MOOSHROOM_TYPE.find(cow, null);
        if (api == null) return;
        api.setMushroomType(key);
    }

    @Override
    public void setPreviousMushroomCowType(MushroomCow cow, ResourceLocation key) {
        MushroomCowTypeApi api = BovineEntityApis.MOOSHROOM_TYPE.find(cow, null);
        if (api == null) return;
        api.setPreviousMushroomTypeKey(key);
    }

    @Override
    public Map<MobEffect, Integer> getLockdownMobEffects(LivingEntity entity) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(entity, null);
        if (api == null) {
            return LockdownEffectAttachment.NO_EFFECTS;
        }
        return api.getLockdownMobEffects();
    }

    @Override
    public void addLockdownMobEffect(LivingEntity entity, MobEffect effect, int duration) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(entity, null);
        if (api == null) return;
        api.addLockdownMobEffect(effect, duration);
    }

    @Override
    public void removeLockdownMobEffect(LivingEntity entity, MobEffect effect) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(entity, null);
        if (api == null) return;
        api.removeLockdownMobEffect(effect);
    }

    @Override
    public void setLockdownMobEffects(LivingEntity entity, Map<MobEffect, Integer> map) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(entity, null);
        if (api == null) return;
        api.setLockdownMobEffects(map);
    }

    @Override
    public void syncLockdownMobEffects(LivingEntity entity) {
        LockdownEffectApi api = BovineEntityApis.LOCKDOWN_EFFECTS.find(entity, null);
        if (api == null) return;
        api.sync();
    }

    @Override
    public Optional<UUID> getMoobloomTarget(Bee bee) {
        FlowerCowTargetApi api = BovineEntityApis.MOOBLOOM_TARGET.find(bee, null);
        if (api == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(api.getMoobloom());
    }

    @Override
    public void setMoobloomTarget(Bee bee, @Nullable UUID uUID) {
        FlowerCowTargetApi api = BovineEntityApis.MOOBLOOM_TARGET.find(bee, null);
        if (api == null) return;
        api.setMoobloom(uUID);
    }
}
