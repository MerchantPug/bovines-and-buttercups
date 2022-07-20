package com.github.merchantpug.bovinesandbuttercups.entity.type.flower;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class FlowerCowLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    public FlowerCowLoader() {
        super(GSON, "moobloom_types");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        FlowerCowTypeRegistry.reset();
        prepared.forEach((id, je) -> {
            try {
                FlowerCowType moobloomEntityType = FlowerCowType.fromJson(id, je.getAsJsonObject());
                if (!FlowerCowTypeRegistry.contains(id)) {
                    FlowerCowTypeRegistry.register(id, moobloomEntityType);
                } else {
                    if (FlowerCowTypeRegistry.get(id).getLoadingPriority() < moobloomEntityType.getLoadingPriority()) {
                        FlowerCowTypeRegistry.update(id, moobloomEntityType);
                    }
                }
            } catch (Exception e) {
                Constants.LOG.error("There was a problem reading Moobloom Type file '" + id.toString() + "' (skipping): " + e.getMessage());
            }
        });
        Constants.LOG.info("Finished loading moobloom types from data files. Registry contains " + FlowerCowTypeRegistry.size() + " moobloom types.");
    }
}
