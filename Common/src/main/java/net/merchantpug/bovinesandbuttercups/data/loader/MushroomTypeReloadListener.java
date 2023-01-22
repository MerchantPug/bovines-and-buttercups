package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Optional;

public class MushroomTypeReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MushroomTypeReloadListener() {
        super(GSON, "bovinesandbuttercups/mushroom_type");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager manager, ProfilerFiller filler) {
        MushroomTypeRegistry.clear();
        jsonElements.forEach((location, jsonElement) -> {
            try {
                MushroomType.CODEC.codec().decode(new Dynamic<>(JsonOps.INSTANCE, jsonElement)).result().ifPresent(pair -> {
                    if (ConfiguredCowTypeRegistry.containsKey(location))
                        MushroomTypeRegistry.update(location, pair.getFirst());
                    else
                        MushroomTypeRegistry.register(location, pair.getFirst());
                });
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load mushroom type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading mushroom types. Successfully loaded {} mushroom types.", MushroomTypeRegistry.size());
    }
}
