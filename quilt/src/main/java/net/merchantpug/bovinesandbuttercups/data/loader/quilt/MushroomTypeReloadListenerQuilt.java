package net.merchantpug.bovinesandbuttercups.data.loader.quilt;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.MushroomTypeReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class MushroomTypeReloadListenerQuilt extends MushroomTypeReloadListener implements IdentifiableResourceReloader {
    @Override
    public ResourceLocation getQuiltId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/mushroom_type");
    }
}
