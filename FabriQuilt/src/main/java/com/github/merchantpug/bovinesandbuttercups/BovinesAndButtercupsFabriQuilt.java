package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import com.github.merchantpug.bovinesandbuttercups.registry.*;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class BovinesAndButtercupsFabriQuilt {
	public static void init() {
		BovinesAndButtercupsXplat.init();
		BovineBlockEntityTypesFabriQuilt.init();
		BovineEntityTypesFabriQuilt.init();
		BovineItemsFabriQuilt.init();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EffectLockdownCommand.register(dispatcher);
		});
	}
}
