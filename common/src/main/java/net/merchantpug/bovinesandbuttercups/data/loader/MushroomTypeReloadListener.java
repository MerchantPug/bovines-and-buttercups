package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.MushroomType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

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
                var dataResult = MushroomType.CODEC.codec().parse(JsonOps.INSTANCE, jsonElement);
                var mushroomType = dataResult.resultOrPartial(s -> {});

                if (dataResult.error().isPresent()) {
                    if (mushroomType.isPresent())
                        BovinesAndButtercups.LOG.warn("Error loading Mushroom Type '{}'. Mushroom Type will only be partially loaded. {}", location, dataResult.error().get().message());
                    else
                        BovinesAndButtercups.LOG.warn("Error loading Mushroom Type '{}'. (Skipping). {}", location, dataResult.error().get().message());
                }

                if (mushroomType.isEmpty() || MushroomTypeRegistry.containsKey(location) && FlowerTypeRegistry.get(location).loadingPriority() > mushroomType.get().loadingPriority()) return;
                if (MushroomTypeRegistry.containsKey(location))
                    MushroomTypeRegistry.update(location, mushroomType.get());
                else
                    MushroomTypeRegistry.register(location, mushroomType.get());
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load mushroom type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading mushroom types. Successfully loaded {} mushroom types.", MushroomTypeRegistry.size());
    }
}
