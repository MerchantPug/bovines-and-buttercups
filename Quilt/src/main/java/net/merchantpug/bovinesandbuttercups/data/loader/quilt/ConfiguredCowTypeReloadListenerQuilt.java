package net.merchantpug.bovinesandbuttercups.data.loader.quilt;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.loader.ConfiguredCowTypeReloadListener;
import net.minecraft.resources.ResourceLocation;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

public class ConfiguredCowTypeReloadListenerQuilt extends ConfiguredCowTypeReloadListener implements IdentifiableResourceReloader {
    @Override
    public ResourceLocation getQuiltId() {
        return BovinesAndButtercups.asResource("bovinesandbuttercups/configured_cow_type");
    }
}
