package com.github.merchantpug.bovinesandbuttercups.data;

import com.github.merchantpug.bovinesandbuttercups.Constants;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerType;
import com.github.merchantpug.bovinesandbuttercups.data.block.flower.FlowerTypeRegistry;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow.FlowerCowType;
import com.github.merchantpug.bovinesandbuttercups.data.entity.flowercow.FlowerCowTypeRegistry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.Map;

public class CowLoader extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    public CowLoader() {
        super(GSON, "cow_types");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> prepared, ResourceManager resourceManager, ProfilerFiller profiler) {
        FlowerCowTypeRegistry.reset();
        FlowerTypeRegistry.reset();
        prepared.forEach((id, je) -> {
            try {
                if (!je.getAsJsonObject().has("type")) {
                    Constants.LOG.error("'type' field not found in cow type '" + id.toString() + "'. Skipping.");
                } else {
                    ResourceLocation type = ResourceLocation.tryParse(je.getAsJsonObject().get("type").getAsString());
                    if (type.equals(Constants.resourceLocation("moobloom"))) {
                        FlowerCowType moobloomEntityType = FlowerCowType.fromJson(id, je.getAsJsonObject());
                        if (!FlowerCowTypeRegistry.contains(id)) {
                            FlowerCowTypeRegistry.register(id, moobloomEntityType);
                        } else {
                            if (FlowerCowTypeRegistry.get(id).getLoadingPriority() < moobloomEntityType.getLoadingPriority()) {
                                FlowerCowTypeRegistry.update(id, moobloomEntityType);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Constants.LOG.error("There was a problem reading Moobloom Type file '" + id.toString() + "' (skipping): " + e.getMessage());
            }
        });
        FlowerTypeRegistry.register(new FlowerType(new ResourceLocation("test", "fire_flower"), "Fire Flower", null, new ResourceLocation("test", "fire_flower"), "bovines", new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 10), true));
        Constants.LOG.info("Finished loading moobloom types from data files. Registry contains " + FlowerCowTypeRegistry.size() + " moobloom types.");
        Constants.LOG.info("Finished loading flower types from data files. Registry contains " + FlowerTypeRegistry.size() + " flower types.");
    }
}
