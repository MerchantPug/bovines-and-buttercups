package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowTypeRegistryUtil;
import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowMyceliumLayer;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRenderer<MushroomCow, CowModel<MushroomCow>> {
    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel<MushroomCow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$initRenderLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new MushroomCowMyceliumLayer(this));
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/MushroomCow;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void bovinesandbuttercups$changeTextureLocation(MushroomCow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation location = Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().cowTexture().isPresent() ? Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().cowTexture().get() : new ResourceLocation(ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.getLevel(), Services.PLATFORM.getMushroomCowTypeFromCow(entity)).getNamespace(), "textures/entity/moobloom/" + ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.getLevel(), Services.PLATFORM.getMushroomCowTypeFromCow(entity)).getPath().toLowerCase(Locale.ROOT) + "_mooshroom.png");
        if (BovinesAndButtercupsClient.LOADED_COW_TEXTURES.contains(location)) {
            cir.setReturnValue(location);
        } else {
            cir.setReturnValue(BovinesAndButtercups.asResource("textures/entity/mooshroom/missing_mooshroom.png"));
        }
    }
}
