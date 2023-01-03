package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.client.item.CustomFlowerItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomHugeMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.SparkleParticle;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.merchantpug.bovinesandbuttercups.registry.BovineModelLayers;
import net.merchantpug.bovinesandbuttercups.registry.BovineParticleTypes;
import net.minecraft.client.model.CowModel;

public class BovinesAndButtercupsClientFabriclike {
    public static void init() {
        BovinesAndButtercupsClient.init();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
        EntityRendererRegistry.register(Services.PLATFORM.getMoobloomEntity(), FlowerCowRenderer::new);

        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomFlowerBlockEntity(), CustomFlowerRenderer::new);
        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomMushroomBlockEntity(), CustomMushroomRenderer::new);
        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomFlowerPotBlockEntity(), CustomFlowerPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomMushroomPotBlockEntity(), CustomMushroomPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(Services.PLATFORM.getCustomHugeMushroomBlockEntity(), CustomHugeMushroomBlockRenderer::new);

        BuiltinItemRendererRegistry.INSTANCE.register(Services.PLATFORM.getCustomFlowerItem(), new CustomFlowerItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(Services.PLATFORM.getCustomMushroomItem(), new CustomMushroomItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(Services.PLATFORM.getCustomHugeMushroomItem(), new CustomHugeMushroomItemRenderer());

        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.MODEL_LOCATION.get(), new ModelLocationParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.SPARKLE.get(), SparkleParticle.Provider::new);
    }
}
