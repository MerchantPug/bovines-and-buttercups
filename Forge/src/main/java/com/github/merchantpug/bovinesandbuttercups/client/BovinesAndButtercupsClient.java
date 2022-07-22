package com.github.merchantpug.bovinesandbuttercups.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommonClient;
import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.FlowerCowRenderer;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BovinesAndButtercupsClient {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        BovinesAndButtercupsCommonClient.init();
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);
    }

    @SubscribeEvent
    public static void registerDataDrivenModelsEvent(ModelEvent.RegisterAdditional event) {
        Map<ResourceLocation, Resource> blocks = Minecraft.getInstance().getResourceManager().listResources("models/bovines", fileName -> fileName.getPath().endsWith(".json"));

        for (ResourceLocation resourceLocation : blocks.keySet()) {
            StringBuilder newId = new StringBuilder(resourceLocation.getPath().replaceFirst("models/bovines/", ""));
            newId.replace(newId.length() - 5 , newId.length(), "");
            event.register(new ModelResourceLocation(new ResourceLocation(resourceLocation.getNamespace(), newId.toString()), "bovines"));
        }
    }
}
