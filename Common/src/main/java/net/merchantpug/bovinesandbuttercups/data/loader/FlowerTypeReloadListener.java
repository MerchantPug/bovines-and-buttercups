package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class FlowerTypeReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public FlowerTypeReloadListener() {
        super(GSON, "bovinesandbuttercups/flower_type");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElements, ResourceManager manager, ProfilerFiller filler) {
        FlowerTypeRegistry.clear();
        jsonElements.forEach((location, jsonElement) -> {
            try {
                var flowerType = FlowerType.CODEC.codec().parse(JsonOps.INSTANCE, jsonElement)
                        .getOrThrow(false, s -> BovinesAndButtercups.LOG.error("Could not load flower type at location '{}'. (Skipping). {}", location, s));
                if (FlowerTypeRegistry.containsKey(location) && FlowerTypeRegistry.get(location).loadingPriority() > flowerType.loadingPriority()) return;
                if (FlowerTypeRegistry.containsKey(location))
                    FlowerTypeRegistry.update(location, flowerType);
                else
                    FlowerTypeRegistry.register(location, flowerType);
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load flower type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading flower type. Successfully loaded {} flower types.", FlowerTypeRegistry.size());
    }
}
