package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.block.renderer.CustomFlowerRenderer;
import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItemRenderer;
import com.github.merchantpug.bovinesandbuttercups.particle.ModelLocationParticle;
import com.github.merchantpug.bovinesandbuttercups.platform.Services;
import com.github.merchantpug.bovinesandbuttercups.registry.*;
import com.github.merchantpug.bovinesandbuttercups.entity.renderer.FlowerCowRenderer;
import com.github.merchantpug.bovinesandbuttercups.networking.BovinePacketsS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.model.CowModel;
import net.minecraft.server.packs.PackType;

public class BovinesAndButtercupsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsXplatClient.init();

        BovinePacketsS2C.register();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
        EntityRendererRegistry.register(Services.PLATFORM.getMoobloomEntity(), FlowerCowRenderer::new);

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CowTextureReloadListenerFabric());

        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomFlowerBlockEntity(), CustomFlowerRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(Services.PLATFORM.getCustomFlowerItem(), new CustomFlowerItemRenderer());
        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.MODEL_LOCATION.get(), new ModelLocationParticle.Provider());
    }
}
