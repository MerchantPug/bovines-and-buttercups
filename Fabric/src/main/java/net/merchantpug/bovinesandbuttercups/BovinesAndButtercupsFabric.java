package net.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypesFabriclike;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.merchantpug.bovinesandbuttercups.registry.BovineItemsFabriclike;
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
        registerCompostables();

        BiomeModifications.addSpawn(biome -> true, MobCategory.CREATURE, BovineEntityTypesFabriclike.MOOBLOOM, 60, 2, 4);
        BiomeModifications.addSpawn(biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS, MobCategory.CREATURE, EntityType.MOOSHROOM, 60, 2, 4);
    }

    private static void registerCompostables() {
        CompostingChanceRegistry.INSTANCE.add(BovineItems.BUTTERCUP.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.PINK_DAISY.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.LIMELIGHT.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.BIRD_OF_PARADISE.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.CHARGELILY.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.HYACINTH.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.SNOWDROP.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.TROPICAL_BLUE.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.FREESIA.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItemsFabriclike.CUSTOM_FLOWER, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItemsFabriclike.CUSTOM_MUSHROOM, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItemsFabriclike.CUSTOM_MUSHROOM_BLOCK, 0.85F);
    }
}
