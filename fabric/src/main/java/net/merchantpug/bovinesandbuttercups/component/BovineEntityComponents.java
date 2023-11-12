package net.merchantpug.bovinesandbuttercups.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.MushroomCow;

public class BovineEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<LockdownEffectComponent> LOCKDOWN_EFFECT_COMPONENT = ComponentRegistry.getOrCreate(BovinesAndButtercups.asResource("lockdown"), LockdownEffectComponent.class);
    public static final ComponentKey<FlowerCowTargetComponent> FLOWER_COW_TARGET_COMPONENT = ComponentRegistry.getOrCreate(BovinesAndButtercups.asResource("moobloom_target"), FlowerCowTargetComponent.class);
    public static final ComponentKey<MushroomCowTypeComponent> MUSHROOM_COW_TYPE_COMPONENT = ComponentRegistry.getOrCreate(BovinesAndButtercups.asResource("mooshroom_type"), MushroomCowTypeComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, LOCKDOWN_EFFECT_COMPONENT, LockdownEffectComponentImpl::new);
        registry.registerFor(Bee.class, FLOWER_COW_TARGET_COMPONENT, FlowerCowTargetComponentImpl::new);
        registry.registerFor(MushroomCow.class, MUSHROOM_COW_TYPE_COMPONENT, MushroomCowTypeComponentImpl::new);
    }
}
