/**
 * MIT License
 *
 * Copyright (c) 2019 simibubi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.particle.SparkleParticle;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowDatapackMushroomLayer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowMyceliumLayer;
import net.merchantpug.bovinesandbuttercups.client.resources.ModFilePackResources;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.util.CowTextureReloadListener;
import net.merchantpug.bovinesandbuttercups.registry.BovineBlockEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineEntityTypes;
import net.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

@Mod.EventBusSubscriber(modid = BovinesAndButtercups.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BovinesAndButtercupsClientForge {
    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        BovinesAndButtercupsClient.init();
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(RegisterClientReloadListenersEvent event) {
        BovinesAndButtercupsClient.registerCowTexturePaths();
        event.registerReloadListener(new CowTextureReloadListener());
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);
        event.registerBlockEntityRenderer(BovineBlockEntityTypes.CUSTOM_FLOWER.get(), CustomFlowerRenderer::new);
        event.registerBlockEntityRenderer(BovineBlockEntityTypes.CUSTOM_MUSHROOM.get(), CustomMushroomRenderer::new);
        event.registerBlockEntityRenderer(BovineBlockEntityTypes.POTTED_CUSTOM_FLOWER.get(), CustomFlowerPotBlockRenderer::new);
        event.registerBlockEntityRenderer(BovineBlockEntityTypes.POTTED_CUSTOM_MUSHROOM.get(), CustomMushroomPotBlockRenderer::new);
        event.registerBlockEntityRenderer(BovineBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.get(), CustomHugeMushroomBlockRenderer::new);
    }

    @SubscribeEvent
    public static void registerRenderLayers(EntityRenderersEvent.AddLayers event) {
        MushroomCowRenderer mushroomCowRenderer = event.getRenderer(EntityType.MOOSHROOM);
        mushroomCowRenderer.addLayer(new MushroomCowDatapackMushroomLayer<>(mushroomCowRenderer, Minecraft.getInstance().getBlockRenderer()));
        mushroomCowRenderer.addLayer(new MushroomCowMyceliumLayer(mushroomCowRenderer));
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.register(BovineParticleTypes.MODEL_LOCATION.get(), new ModelLocationParticle.Provider());
        event.register(BovineParticleTypes.SPARKLE.get(), SparkleParticle.Provider::new);
    }

    @SubscribeEvent
    public static void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            IModFileInfo modFileInfo = ModList.get().getModFileById(BovinesAndButtercups.MOD_ID);
            if (modFileInfo == null) {
                BovinesAndButtercups.LOG.error("Could not find Bovines and Buttercups mod file info; built-in resource packs will be missing!");
                return;
            }
            IModFile modFile = modFileInfo.getFile();
            event.addRepositorySource((consumer, constructor) -> {
                consumer.accept(Pack.create(BovinesAndButtercups.asResource("mojang").toString(), false, () -> new ModFilePackResources("Moobloom Mojang Textures", modFile, "resourcepacks/mojang"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
                consumer.accept(Pack.create(BovinesAndButtercups.asResource("no_grass").toString(), false, () -> new ModFilePackResources("No Grass Back", modFile, "resourcepacks/no_grass"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
                consumer.accept(Pack.create(BovinesAndButtercups.asResource("no_buds").toString(), false, () -> new ModFilePackResources("No Buds", modFile, "resourcepacks/no_buds"), constructor, Pack.Position.TOP, PackSource.DEFAULT));
            });
        }
    }
}
