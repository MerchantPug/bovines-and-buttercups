package net.merchantpug.bovinesandbuttercups.data.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.data.ConfiguredCowTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.FlowerTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.MushroomTypeRegistry;
import net.merchantpug.bovinesandbuttercups.data.block.FlowerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;
import java.util.Optional;

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
                Optional<FlowerType> flowerType = FlowerType.CODEC.codec().parse(new Dynamic<>(JsonOps.INSTANCE, jsonElement)).result();
                if (flowerType.isEmpty()) return;
                if (ConfiguredCowTypeRegistry.containsKey(location))
                    FlowerTypeRegistry.update(location, flowerType.get());
                else
                    FlowerTypeRegistry.register(location, flowerType.get());
            } catch (Exception ex) {
                BovinesAndButtercups.LOG.error("Could not load flower type at location '{}'. (Skipping). {}", location, ex);
            }
        });
        BovinesAndButtercups.LOG.info("Finished loading flower type. Successfully loaded {} flower types.", FlowerTypeRegistry.size());
    }
}
