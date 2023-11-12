package net.merchantpug.bovinesandbuttercups.mixin.neoforge.client;

import net.merchantpug.bovinesandbuttercups.access.ModelBakeryAccess;
import net.merchantpug.bovinesandbuttercups.client.util.BovineStateModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin implements ModelBakeryAccess {
    @Shadow protected abstract void cacheAndQueueDependencies(ResourceLocation resourceLocation, UnbakedModel unbakedModel);

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void cancelBovineModelLoad(ResourceLocation key, CallbackInfo ci) {
        if (key instanceof ModelResourceLocation modelKey && modelKey.getVariant().startsWith("bovinesandbuttercups_")) {
            UnbakedModel model = BovineStateModelUtil.getVariantModel(Minecraft.getInstance().getResourceManager(), modelKey);
            cacheAndQueueDependencies(modelKey, model);
            ci.cancel();
        }
    }

    @Unique
    Map<ResourceLocation, AtlasSet.StitchResult> bovinesandbuttercups$stitchResults = null;

    @Override
    public Map<ResourceLocation, AtlasSet.StitchResult> bovinesandbuttercups$getStitchResults() {
        return bovinesandbuttercups$stitchResults;
    }

    @Override
    public void bovinesandbuttercups$setStitchResults(Map<ResourceLocation, AtlasSet.StitchResult> value) {
        this.bovinesandbuttercups$stitchResults = value;
    }

    @Override
    public void bovinesandbuttercups$resetStitchResults() {
        bovinesandbuttercups$stitchResults = null;
    }

}
