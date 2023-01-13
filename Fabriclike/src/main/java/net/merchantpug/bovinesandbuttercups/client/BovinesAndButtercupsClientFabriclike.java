package net.merchantpug.bovinesandbuttercups.client;

import net.merchantpug.bovinesandbuttercups.client.item.CustomFlowerItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomHugeMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.SparkleParticle;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.minecraft.client.model.CowModel;

public class BovinesAndButtercupsClientFabriclike {
    public static void init() {
        BovinesAndButtercupsClient.init();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
        EntityRendererRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);

        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_FLOWER.get(), CustomFlowerRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_MUSHROOM.get(), CustomMushroomRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.POTTED_CUSTOM_FLOWER.get(), CustomFlowerPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.POTTED_CUSTOM_MUSHROOM.get(), CustomMushroomPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.get(), CustomHugeMushroomBlockRenderer::new);

        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_FLOWER.get(), new CustomFlowerItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_MUSHROOM.get(), new CustomMushroomItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_MUSHROOM_BLOCK.get(), new CustomHugeMushroomItemRenderer());

        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.MODEL_LOCATION.get(), new ModelLocationParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.SPARKLE.get(), SparkleParticle.Provider::new);
    }
}
