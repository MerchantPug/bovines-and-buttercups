package net.merchantpug.bovinesandbuttercups;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.merchantpug.bovinesandbuttercups.data.loader.quilt.ConfiguredCowTypeReloadListenerQuilt;
import net.merchantpug.bovinesandbuttercups.data.loader.quilt.FlowerTypeReloadListenerQuilt;
import net.merchantpug.bovinesandbuttercups.data.loader.quilt.MushroomTypeReloadListenerQuilt;
import net.merchantpug.bovinesandbuttercups.network.s2c.SyncDatapackContentsPacket;
import net.merchantpug.bovinesandbuttercups.registry.BovineCowTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineTags;
import net.merchantpug.bovinesandbuttercups.util.HolderUtil;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.ResourcePackActivationType;
import org.quiltmc.qsl.worldgen.biome.api.BiomeModifications;
import org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext;
import org.quiltmc.qsl.worldgen.biome.api.ModificationPhase;

import java.util.HashMap;
import java.util.function.Predicate;

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
        BovinesAndButtercupsFabriclike.init();

        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new ConfiguredCowTypeReloadListenerQuilt());
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new FlowerTypeReloadListenerQuilt());
        ResourceLoader.get(PackType.SERVER_DATA).registerReloader(new MushroomTypeReloadListenerQuilt());

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
                biome -> BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.FLOWER_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && HolderUtil.containsBiomeHolder(biome.getBiomeHolder(), configuredCowType.getConfiguration().getSettings().biomes().get()) && configuredCowType.getConfiguration().getSettings().naturalSpawnWeight() > 0),
                BovineEntityTypes.MOOBLOOM.get(), 15, 4, 4);
        createBiomeModifications(BovinesAndButtercups.asResource("mooshroom"),
                biome -> biome.getBiomeKey() != Biomes.MUSHROOM_FIELDS && BovineRegistryUtil.configuredCowTypeStream().anyMatch(configuredCowType -> configuredCowType.getCowType() == BovineCowTypes.MUSHROOM_COW_TYPE.get() && configuredCowType.getConfiguration().getSettings().biomes().isPresent() && HolderUtil.containsBiomeHolder(biome.getBiomeHolder(), configuredCowType.getConfiguration().getSettings().biomes().get()) && configuredCowType.getConfiguration().getSettings().naturalSpawnWeight() > 0),
                EntityType.MOOSHROOM, 15, 4, 4);
        BiomeModifications.create(BovinesAndButtercups.asResource("remove_cows")).add(ModificationPhase.REMOVALS, biome -> biome.isIn(BovineTags.PREVENT_COW_SPAWNS), context -> context.getSpawnSettings().removeSpawnsOfEntityType(EntityType.COW));

        ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercups.asResource("mojang"), mod, ResourcePackActivationType.NORMAL, Component.literal("Mojang Textures"));
        ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_grass"), mod, ResourcePackActivationType.NORMAL, Component.literal("No Grass Back"));
        ResourceLoader.registerBuiltinResourcePack(BovinesAndButtercups.asResource("no_buds"), mod, ResourcePackActivationType.NORMAL, Component.literal("No Buds"));
    }

    public static void createBiomeModifications(ResourceLocation location, Predicate<BiomeSelectionContext> predicate, EntityType<?> entityType, int weight, int min, int max) {
        BiomeModifications.create(location).add(ModificationPhase.POST_PROCESSING, predicate, context -> context.getSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(entityType, weight, min, max)));
    }
}