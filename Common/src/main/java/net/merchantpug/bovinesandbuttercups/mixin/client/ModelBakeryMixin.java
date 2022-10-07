package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow protected abstract BlockModel loadBlockModel(ResourceLocation resourceLocation) throws IOException;

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> unbakedCache;

    @Shadow protected abstract void cacheAndQueueDependencies(ResourceLocation p_119353_, UnbakedModel p_119354_);

    @Inject(method = "loadModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelResourceLocation;getVariant()Ljava/lang/String;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$loadMoobloomModel(ResourceLocation resourceLocation, CallbackInfo ci, ModelResourceLocation modelResourceLocation) throws IOException {
        if (Objects.equals(modelResourceLocation.getVariant(), "bovines")) {
            ResourceLocation resourceLocation2 = new ResourceLocation(resourceLocation.getNamespace(), "bovines/" + resourceLocation.getPath());
            BlockModel blockModel = this.loadBlockModel(resourceLocation2);
            this.cacheAndQueueDependencies(modelResourceLocation, blockModel);
            this.unbakedCache.put(modelResourceLocation, blockModel);
            ci.cancel();
        }
    }
}
