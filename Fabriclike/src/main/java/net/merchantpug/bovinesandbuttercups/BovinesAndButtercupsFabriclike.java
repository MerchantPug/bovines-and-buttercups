package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.merchantpug.bovinesandbuttercups.registry.*;

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
	}
}
