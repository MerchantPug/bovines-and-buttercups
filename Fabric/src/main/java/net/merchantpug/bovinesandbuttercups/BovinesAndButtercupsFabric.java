package net.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.loader.fabric.ConfiguredCowTypeReloadListenerFabric;
import net.merchantpug.bovinesandbuttercups.data.loader.fabric.FlowerTypeReloadListenerFabric;
import net.merchantpug.bovinesandbuttercups.data.loader.fabric.MushroomTypeReloadListenerFabric;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineItems;
import net.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

import java.util.HashMap;
import java.util.function.Predicate;

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

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ConfiguredCowTypeReloadListenerFabric());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FlowerTypeReloadListenerFabric());
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new MushroomTypeReloadListenerFabric());

        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) -> {
            HashMap<ResourceLocation, ConfiguredCowType<?, ?>> configuredCowTypeMap = new HashMap<>();
            ConfiguredCowTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue().equals(BovineRegistryUtil.getDefaultMoobloom(entry.getValue().getCowType()))) return;
                configuredCowTypeMap.put(entry.getKey(), entry.getValue());
            });

            HashMap<ResourceLocation, FlowerType> flowerTypeMap = new HashMap<>();
            FlowerTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue() == FlowerType.MISSING) return;
                flowerTypeMap.put(entry.getKey(), entry.getValue());
            });

            HashMap<ResourceLocation, MushroomType> mushroomTypeMap = new HashMap<>();
            MushroomTypeRegistry.asStream().forEach(entry -> {
                if (entry.getValue() == MushroomType.MISSING) return;
                mushroomTypeMap.put(entry.getKey(), entry.getValue());
            });

            var packet = new SyncDatapackContentsPacket(configuredCowTypeMap, flowerTypeMap, mushroomTypeMap);
            ServerPlayNetworking.send(player, packet.getId(), packet.toBuf());
        });

        createBiomeModifications(BovinesAndButtercups.asResource("moobloom"),
                biome -> BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.FLOWER_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && configuredCowType.getConfiguration().getSettings().biomes().get().contains(biome.getBiomeRegistryEntry())),
                BovineEntityTypes.MOOBLOOM.get(), 15, 4, 4);
        createBiomeModifications(BovinesAndButtercups.asResource("mooshroom"),
                biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS && BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.MUSHROOM_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && configuredCowType.getConfiguration().getSettings().biomes().get().contains(biome.getBiomeRegistryEntry())),
                EntityType.MOOSHROOM, 15, 4, 4);
        BiomeModifications.create(BovinesAndButtercups.asResource("remove_cows")).add(ModificationPhase.REMOVALS, biome -> biome.hasTag(BovineTags.PREVENT_COW_SPAWNS), context -> context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.COW));
    }

    public static void createBiomeModifications(ResourceLocation location, Predicate<BiomeSelectionContext> predicate, EntityType<?> entityType, int weight, int min, int max) {
        BiomeModifications.create(location).add(ModificationPhase.POST_PROCESSING, predicate, context -> context.getSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(entityType, weight, min, max)));
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
        CompostingChanceRegistry.INSTANCE.add(BovineItems.CUSTOM_FLOWER.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.CUSTOM_MUSHROOM.get(), 0.65F);
        CompostingChanceRegistry.INSTANCE.add(BovineItems.CUSTOM_MUSHROOM_BLOCK.get(), 0.85F);
    }
}
