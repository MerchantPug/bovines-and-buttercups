package net.merchantpug.bovinesandbuttercups.mixin.fabriclike.client;

import net.merchantpug.bovinesandbuttercups.client.util.BovineStateModelUtil;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> unbakedCache;

    @Shadow @Final private ResourceManager resourceManager;

    @Shadow @Final private Map<ResourceLocation, UnbakedModel> topLevelModels;

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadTopLevel(Lnet/minecraft/client/resources/model/ModelResourceLocation;)V", ordinal = 3, shift = At.Shift.AFTER))
    private void bovinesandbuttercups$initModels(ResourceManager resourceManager, BlockColors blockColors, ProfilerFiller profilerFiller, int maxMipmapLevel, CallbackInfo ci) {
        BovineStateModelUtil.initModels((ModelBakery)(Object)this, this.resourceManager, this.unbakedCache, this.topLevelModels);
    }
}
