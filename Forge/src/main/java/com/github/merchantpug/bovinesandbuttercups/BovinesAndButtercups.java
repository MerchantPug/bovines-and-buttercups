package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.data.CowLoader;
import com.github.merchantpug.bovinesandbuttercups.network.BovineForgePacketHandler;
import com.github.merchantpug.bovinesandbuttercups.registry.BiomeModifierSerializerRegistry;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineItemsForge;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(Constants.MOD_ID)
public class BovinesAndButtercups {
    public BovinesAndButtercups() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BovinesAndButtercupsCommon.VERSION = ModLoadingContext.get().getActiveContainer().getModInfo().getVersion().toString();

        BovinesAndButtercupsCommon.init();
        BovineItemsForge.init();
        BiomeModifierSerializerRegistry.init(eventBus);
        eventBus.addListener((EntityAttributeCreationEvent event) -> {
            event.put(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes().build());
        });
        eventBus.addListener((FMLCommonSetupEvent event) -> {
            BovineForgePacketHandler.init();
            event.enqueueWork(() -> SpawnPlacements.register(BovineEntityTypes.MOOBLOOM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlowerCow::canMoobloomSpawn));
        });
        MinecraftForge.EVENT_BUS.addListener((RegisterCommandsEvent event) -> {
            EffectLockdownCommand.register(event.getDispatcher());
        });
        MinecraftForge.EVENT_BUS.addListener((AddReloadListenerEvent event) -> {
            CowLoader loader = new CowLoader();
            event.addListener(loader);
        });
    }
}