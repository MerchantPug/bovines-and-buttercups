package net.merchantpug.bovinesandbuttercups;

import net.merchantpug.bovinesandbuttercups.capabilities.MushroomCowTypeCapabilityAttacher;
import net.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import net.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(BovinesAndButtercups.MOD_ID)
public class BovinesAndButtercupsForge {
    public BovinesAndButtercupsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercups.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovinesAndButtercups.init();
        BovineBlockEntityTypesForge.init();
        BovineEntityTypesForge.init();
        BovineItemsForge.init();
        BovineRegistriesForge.init(eventBus);
        BiomeModifierSerializerRegistry.init(eventBus);

        this.addEventListeners();
    }

    private void addEventListeners() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(Services.PLATFORM.getMoobloomEntity(), FlowerCow.createAttributes().build());
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            event.enqueueWork(() -> SpawnPlacements.register(Services.PLATFORM.getMoobloomEntity(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn));
        });

        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, MushroomCowTypeCapabilityAttacher::attach);

        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            EffectLockdownCommand.register(event.getDispatcher());
        });
    }
}