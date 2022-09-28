package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypesFabriQuilt;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypesFabriQuilt;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriQuilt;
import net.merchantpug.bovinesandbuttercups.registry.BovineItemsFabriQuilt;

public class BovinesAndButtercupsFabriQuilt {
	public static void init() {
		BovinesAndButtercups.init();

		BovineCowTypesFabriQuilt.register();
		BovineBlockEntityTypesFabriQuilt.init();
		BovineEntityTypesFabriQuilt.init();
		BovineItemsFabriQuilt.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EffectLockdownCommand.register(dispatcher);
		});
	}
}
