package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
                Optional<ConfiguredCowType<?, ?>> configuredCowType = ConfiguredCowType.CODEC.parse(JsonOps.INSTANCE, jsonElement).result();
                if (configuredCowType.isEmpty()) return;
                if (ConfiguredCowTypeRegistry.containsKey(location))
                    ConfiguredCowTypeRegistry.update(location, configuredCowType.get());
                else
                    ConfiguredCowTypeRegistry.register(location, configuredCowType.get());
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load Configured Cow Type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading configured cow types. Successfully loaded {} configured cow types.", ConfiguredCowTypeRegistry.size());
    }
}
