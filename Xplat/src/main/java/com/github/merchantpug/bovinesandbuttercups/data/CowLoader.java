package com.github.merchantpug.bovinesandbuttercups.data;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.api.ICowType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class CowLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    public CowLoader() {
        super(GSON, "cow_types");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        CowTypeRegistry.reset();
        FlowerTypeRegistry.reset();
        prepared.forEach((id, je) -> {
            try {
                if (!je.getAsJsonObject().has("type")) {
                    throw new NullPointerException("'type' field not found in cow type.");
                } else {
                    ResourceLocation type = ResourceLocation.tryParse(je.getAsJsonObject().get("type").getAsString());
                    ICowType cowType = CowTypeRegistry.getTypeFromId(type);
                    if (cowType == null) {
                        throw new NullPointerException("'type' field value '" + type + "' not found.");
                    }
                    ICowType.register(cowType, id, je.getAsJsonObject());
                }
            } catch (Exception e) {
                Constants.LOG.error("There was a problem reading Cow Type file '" + id.toString() + "' (skipping): " + e.getMessage());
            }
        });
        Constants.LOG.info("Finished loading cow types from data files. Registry contains " + CowTypeRegistry.size() + " cow types.");
    }
}
