package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriclike;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;

public class BovinesAndButtercupsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        FabricLoader.getInstance().getModContainer(BovinesAndButtercups.MOD_ID).ifPresent(modContainer -> {
            String version = modContainer.getMetadata().getVersion().getFriendlyString();
            if (version.contains("+")) {
                version = version.split("\\+")[0];
            }
            if (version.contains("-")) {
                version = version.split("-")[0];
            }
            BovinesAndButtercups.VERSION = version;

            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("mojang"), modContainer, "Mojang Textures", ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_grass"), modContainer, "No Grass Back", ResourcePackActivationType.NORMAL);
            ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_buds"), modContainer, "No Buds", ResourcePackActivationType.NORMAL);
        });
        BovinesAndButtercupsFabriclike.init();

        BiomeModifications.addSpawn(biome -> true, MobCategory.CREATURE, BovineEntityTypesFabriclike.MOOBLOOM, 60, 2, 4);
        BiomeModifications.addSpawn(biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS, MobCategory.CREATURE, EntityType.MOOSHROOM, 60, 2, 4);
    }
}
