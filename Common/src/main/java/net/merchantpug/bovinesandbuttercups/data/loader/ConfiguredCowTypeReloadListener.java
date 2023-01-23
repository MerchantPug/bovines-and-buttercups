package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class ConfiguredCowTypeReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public ConfiguredCowTypeReloadListener() {
        super(GSON, "bovinesandbuttercups/configured_cow_type");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager manager, ProfilerFiller filler) {
        ConfiguredCowTypeRegistry.clear();
        jsonElements.forEach((location, jsonElement) -> {
            try {
                var configuredCowType = ConfiguredCowType.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, (s -> BovinesAndButtercups.LOG.error("Could not load Configured Cow Type at location '{}'. (Skipping). {}", location, s)));
                if (ConfiguredCowTypeRegistry.get(location).isPresent() && ConfiguredCowTypeRegistry.get(location).get().getLoadingPriority() > configuredCowType.getLoadingPriority()) return;
                configuredCowType.getConfiguration().getSettings().biomes().ifPresent(biomeTagKey -> BovinesAndButtercups.LOG.info(biomeTagKey.location()));
                if (ConfiguredCowTypeRegistry.containsKey(location))
                    ConfiguredCowTypeRegistry.update(location, configuredCowType);
                else
                    ConfiguredCowTypeRegistry.register(location, configuredCowType);
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load Configured Cow Type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading configured cow types. Successfully loaded {} configured cow types.", ConfiguredCowTypeRegistry.size());
    }
}
