package net.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.content.command.EffectLockdownCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.merchantpug.bovinesandbuttercups.component.BovineEntityComponents;
import net.merchantpug.bovinesandbuttercups.component.MushroomCowTypeComponent;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.MushroomCowSpawnUtil;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.biome.Biomes;

public class BovinesAndButtercupsFabriclike {
	public static void init() {
		BovineRegistriesFabriclike.init();
		BovineCowTypes.registerDefaultConfigureds();

		BovinesAndButtercups.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> EffectLockdownCommand.register(dispatcher, context));

		ServerLifecycleEvents.SERVER_STARTING.register(BovinesAndButtercups::setServer);

		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			if (BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.isProvidedBy(entity)) {
				MushroomCowTypeComponent component = BovineEntityComponents.MUSHROOM_COW_TYPE_COMPONENT.get(entity);
				if (component.getMushroomCowTypeKey() == null) {
					if (MushroomCowSpawnUtil.getTotalSpawnWeight(level, entity.blockPosition()) > 0) {
							component.setMushroomCowType(MushroomCowSpawnUtil.getMooshroomSpawnTypeDependingOnBiome(level, entity.blockPosition(), level.getRandom()));
					} else if (BovineRegistryUtil.configuredCowTypeStream().anyMatch(cct -> cct.getConfiguration() instanceof MushroomCowConfiguration mcct && mcct.usesVanillaSpawningHack()) && level.getBiome(entity.blockPosition()).is(Biomes.MUSHROOM_FIELDS)) {
						if (((MushroomCow)entity).getVariant().equals(MushroomCow.MushroomType.BROWN)) {
							component.setMushroomCowType(BovinesAndButtercups.asResource("brown_mushroom"));
						} else {
							component.setMushroomCowType(BovinesAndButtercups.asResource("red_mushroom"));
						}
					} else {
							component.setMushroomCowType(MushroomCowSpawnUtil.getMooshroomSpawnType(level.getRandom()));
					}
				}
			}
		});
	}
}
