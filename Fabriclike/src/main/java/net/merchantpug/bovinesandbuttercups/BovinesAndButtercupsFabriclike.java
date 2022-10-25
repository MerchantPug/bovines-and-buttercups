package net.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.component.MushroomCowTypeComponent;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.world.entity.animal.MushroomCow;

public class BovinesAndButtercupsFabriclike {
	public static void init() {
		BovinesAndButtercups.init();

		BovineRegistriesFabriclike.register();
		BovineBlockEntityTypesFabriclike.init();
		BovineEntityTypesFabriclike.init();
		BovineItemsFabriclike.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EffectLockdownCommand.register(dispatcher);
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			if (BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.isProvidedBy(entity)) {
				MushroomCowTypeComponent component = BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(entity);
				if (component.getMushroomCowTypeKey() == null) {
					if (BovineRegistryUtil.configuredCowTypeStream(level).filter(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration).anyMatch(cct -> cct.getConfiguration().getNaturalSpawnWeight() > 0)) {
						if (MushroomCowSpawnUtil.getTotalSpawnWeight(level, entity.blockPosition()) > 0) {
							component.setMushroomCowType(MushroomCowSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(level, entity.blockPosition(), level.getRandom()));
						} else {
							component.setMushroomCowType(MushroomCowSpawnUtil.getMooshroomSpawnType(level, level.getRandom()));
						}
					} else {
						if (((MushroomCow)entity).getMushroomType().equals(MushroomCow.MushroomType.BROWN)) {
							component.setMushroomCowType(BovinesAndButtercups.asResource("brown_mushroom"));
						} else {
							component.setMushroomCowType(BovinesAndButtercups.asResource("red_mushroom"));
						}
					}
				}
			}
		});
	}
}