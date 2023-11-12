package net.merchantpug.bovinesandbuttercups.mixin.neoforge.client;

import net.merchantpug.bovinesandbuttercups.client.util.BovineStateModelUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow protected abstract void cacheAndQueueDependencies(ResourceLocation resourceLocation, UnbakedModel unbakedModel);

    @Inject(method = "loadModel", at = @At("HEAD"), cancellable = true)
    private void cancelBovineModelLoad(ResourceLocation key, CallbackInfo ci) {
        if (key instanceof ModelResourceLocation modelKey && modelKey.getVariant().startsWith("bovinesandbuttercups_")) {
            UnbakedModel model = BovineStateModelUtil.getVariantModel(Minecraft.getInstance().getResourceManager(), modelKey);
            cacheAndQueueDependencies(modelKey, model);
            ci.cancel();
        }
    }

}
