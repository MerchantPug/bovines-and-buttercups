package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
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
public abstract class ModelBakeryFabricMixin {

    @Shadow protected abstract void loadTopLevel(ModelResourceLocation modelResourceLocation);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 2))
    private void initMoobloomFlowers(ResourceManager resourceManager, BlockColors blockColors, ProfilerFiller profilerFiller, int maxMipmapLevel, CallbackInfo ci) {
        profilerFiller.popPush("bovines");
        Map<ResourceLocation, Resource> blocks = resourceManager.listResources("models/bovines", fileName -> fileName.getPath().endsWith(".json"));

        for (ResourceLocation resourceLocation : blocks.keySet()) {
            StringBuilder newId = new StringBuilder(resourceLocation.getPath().replaceFirst("models/bovines/", ""));
            newId.replace(newId.length() - 5 , newId.length(), "");
            this.loadTopLevel(new ModelResourceLocation(new ResourceLocation(resourceLocation.getNamespace(), newId.toString()), "bovines"));
        }
    }
}
