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
import net.merchantpug.bovinesandbuttercups.client.particle.BloomParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.ShroomParticle;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowDatapackMushroomLayer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowMyceliumLayer;
import net.merchantpug.bovinesandbuttercups.client.resources.ModFilePackResources;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.util.CowTextureReloadListener;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.merchantpug.bovinesandbuttercups.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
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

import java.nio.file.Path;

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
    public static void registerColorHandlers(RegisterColorHandlersEvent.Item event) {
        event.register((stack, i) ->
                        i == 0 ? -1 : NectarBowlItem.getCowTypeFromStack(stack) != null ? Color.asInt(Color.saturateForNectar(NectarBowlItem.getCowTypeFromStack(stack).getConfiguration().getColor())) : -1,
                        BovineItems.NECTAR_BOWL.get());
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
        event.register(BovineParticleTypes.BLOOM.get(), BloomParticle.Provider::new);
        event.register(BovineParticleTypes.SHROOM.get(), ShroomParticle.Provider::new);
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
            event.addRepositorySource((consumer) -> {
                consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("mojang").toString(), Component.translatable("resourcePack.bovinesandbuttercups.mojang.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/mojang", modFile, Path.of("resourcepacks/mojang")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_grass").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noGrass.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/no_grass", modFile, Path.of("resourcepacks/no_grass")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
                consumer.accept(Pack.readMetaAndCreate(BovinesAndButtercups.asResource("no_buds").toString(), Component.translatable("resourcePack.bovinesandbuttercups.noBuds.name"), false, (s) -> new ModFilePackResources("bovinesandbuttercups/no_buds", modFile, Path.of("resourcepacks/no_buds")), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.DEFAULT));
            });
        }
    }
}
