package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowLoaderQuilt;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineSpawnRestrictions;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.MobCategory;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.command.api.CommandRegistrationCallback;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;

public class BovinesAndButtercups implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		BovinesAndButtercupsCommon.init();
		BovineSpawnRestrictions.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EffectLockdownCommand.register(dispatcher);
		});

		ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercupsCommon.resourceLocation("mojang"), mod, ResourcePackActivationType.NORMAL, Component.literal("Mojang Textures"));
		ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercupsCommon.resourceLocation("no_grass"), mod, ResourcePackActivationType.NORMAL, Component.literal("No Grass Back"));

		BiomeModifications.addSpawn(biomeSelectionContext -> true, MobCategory.CREATURE, BovineEntityTypes.MOOBLOOM.get(), 60, 2, 4);
		FabricDefaultAttributeRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes());

		ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new FlowerCowLoaderQuilt());
	}
}
