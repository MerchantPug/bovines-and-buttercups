package net.merchantpug.bovinesandbuttercups.data.loader.quilt;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.FlowerTypeReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class FlowerTypeReloadListenerQuilt extends FlowerTypeReloadListener implements IdentifiableResourceReloader {
    @Override
    public ResourceLocation getQuiltId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/flower_type");
    }
}