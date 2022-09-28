package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriQuilt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobCategory;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;

public class BovinesAndButtercupsQuilt implements ModInitializer {

    @Override
    public void onInitialize(ModContainer mod) {
        String version = mod.metadata().version().raw();
        if (version.contains("+")) {
            version = version.split("\\+")[0];
        }
        if (version.contains("-")) {
            version = version.split("-")[0];
        }
        BovinesAndButtercups.VERSION = version;
        BovinesAndButtercupsFabriQuilt.init();

        BiomeModifications.addSpawn(biome -> true, MobCategory.CREATURE, BovineEntityTypesFabriQuilt.MOOBLOOM, 60, 2, 4);

        ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercups.asResource("mojang"), mod, ResourcePackActivationType.NORMAL, Component.literal("Mojang Textures"));
        ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_grass"), mod, ResourcePackActivationType.NORMAL, Component.literal("No Grass Back"));
    }
}