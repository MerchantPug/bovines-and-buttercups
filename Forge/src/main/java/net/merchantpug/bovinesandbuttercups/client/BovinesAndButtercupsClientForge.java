package net.merchantpug.bovinesandbuttercups.client;

import com.mojang.math.Vector3f;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.client.particle.BloomParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.ShroomParticle;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowDatapackMushroomLayer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowMyceliumLayer;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.util.CowTextureReloadListener;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.merchantpug.bovinesandbuttercups.util.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

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
                        i == 0 ? -1 : Color.asInt(Color.saturateForNectar(NectarBowlItem.getCowTypeFromStack(stack) != null ? NectarBowlItem.getCowTypeFromStack(stack).getConfiguration().getColor() : new Vector3f(253.0F / 255.0F, 213.0F / 255.0F, 0.0F / 255.0F))),
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
}
