package net.merchantpug.bovinesandbuttercups.mixin.neoforge.client;

import net.merchantpug.bovinesandbuttercups.access.ModelBakeryAccess;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ModelManager.class)
public class ModelManagerMixin {

    @Inject(method = "loadModels", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;asMap()Ljava/util/Map;"))
    private void bovinesandbuttercups$captureIntoModelBakery(ProfilerFiller filler, Map<ResourceLocation, AtlasSet.StitchResult> stitchResultMap, ModelBakery bakery, CallbackInfoReturnable<ModelManager.ReloadState> cir) {
        ((ModelBakeryAccess)bakery).bovinesandbuttercups$setStitchResults(stitchResultMap);
    }

}
