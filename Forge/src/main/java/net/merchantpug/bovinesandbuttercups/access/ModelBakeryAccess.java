package net.merchantpug.bovinesandbuttercups.access;

import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface ModelBakeryAccess {

    Map<ResourceLocation, AtlasSet.StitchResult> bovinesandbuttercups$getStitchResults();
    void bovinesandbuttercups$setStitchResults(Map<ResourceLocation, AtlasSet.StitchResult> value);
    void bovinesandbuttercups$resetStitchResults();

}
