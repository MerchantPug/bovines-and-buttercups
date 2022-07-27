package com.github.merchantpug.bovinesandbuttercups.client;

import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import com.github.merchantpug.bovinesandbuttercups.BovinesAndButtercupsCommonClient;
import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.client.renderer.FlowerCowRenderer;
import com.github.merchantpug.bovinesandbuttercups.client.resources.CowTextureReloadListener;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import com.github.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BovinesAndButtercupsClient {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        BovinesAndButtercupsCommonClient.init();
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new CowTextureReloadListener());
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

    public static Pack createMojangMoobloomPack(Pack.PackConstructor constructor) {
        try {
            InputStream inputStream = BovinesAndButtercups.class.getClassLoader().getResourceAsStream("resourcepacks/bovinesandbuttercups/mojang.zip");
            File file = new File("./bovinestemp", "mojang.zip");

            if (inputStream != null && !Files.exists(file.toPath())) {
                Path path = Path.of(".", "bovinestemp");
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                Files.copy(inputStream, file.toPath());
                path.toFile().deleteOnExit();
                file.deleteOnExit();
            }

            return Pack.create("bovinesandbuttercups/mojang", false, () -> new FilePackResources(file) {
                public String getName() {
                    return "Mojang Textures";
                }
            }, constructor, Pack.Position.TOP, PackSource.BUILT_IN);
        } catch (Exception ex) {
            Constants.LOG.warn("Could not load Bovines and Buttercups Mojang resource pack. Will not register it as a built-in resource pack.", ex);
            return null;
        }
    }

    public static Pack createNoGrassPack(Pack.PackConstructor constructor) {
        try {
            InputStream inputStream = BovinesAndButtercups.class.getClassLoader().getResourceAsStream("resourcepacks/bovinesandbuttercups/no_grass.zip");
            File file = new File("./bovinestemp", "no_grass.zip");

            if (inputStream != null && !Files.exists(file.toPath())) {
                Path path = Path.of(".", "bovinestemp");
                if (!Files.exists(path)) {
                    Files.createDirectory(path);
                }
                Files.copy(inputStream, file.toPath());
                path.toFile().deleteOnExit();
                file.deleteOnExit();
            }

            return Pack.create("bovinesandbuttercups/no_grass", false, () -> new FilePackResources(file) {
                public String getName() {
                    return "No Grass Back";
                }
            }, constructor, Pack.Position.TOP, PackSource.BUILT_IN);
        } catch (Exception ex) {
            Constants.LOG.warn("Could not load Bovines and Buttercups No Grass resource pack. Will not register it as a built-in resource pack.", ex);
            return null;
        }
    }
}
