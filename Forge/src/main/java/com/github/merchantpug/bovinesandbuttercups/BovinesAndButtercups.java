package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowLoader;
import com.github.merchantpug.bovinesandbuttercups.network.BovineForgePacketHandler;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MOD_ID)
public class BovinesAndButtercups {
    public BovinesAndButtercups() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercupsCommon.init();
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovineForgePacketHandler.init();
            SpawnPlacements.register(BovineEntityTypes.MOOBLOOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn);
        });
        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes().build());
        });
        MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
    }

    @SubscribeEvent
    public void addReloadListeners(AddReloadListenerEvent event) {
        FlowerCowLoader loader = new FlowerCowLoader();
        event.addListener(loader);
    }
}