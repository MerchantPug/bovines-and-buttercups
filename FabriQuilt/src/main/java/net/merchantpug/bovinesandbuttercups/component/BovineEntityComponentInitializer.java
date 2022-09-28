package net.merchantpug.bovinesandbuttercups.component;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.world.entity.animal.MushroomCow;

public class BovineEntityComponentInitializer implements EntityComponentInitializer {
    public static final ComponentKey<MushroomCowTypeComponent> MUSHROOM_COW_TYPE_COMPONENT = ComponentRegistry.getOrCreate(BovinesAndButtercups.asResource("cow_type"), MushroomCowTypeComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(MushroomCow.class, MUSHROOM_COW_TYPE_COMPONENT, MushroomCowTypeComponentImpl::new);
    }
}
