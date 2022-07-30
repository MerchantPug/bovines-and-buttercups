package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import com.github.merchantpug.bovinesandbuttercups.entity.type.renderer.feature.MushroomCowMyceliumLayer;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowRenderer.class)
public abstract class MushroomCowRendererMixin extends MobRenderer<MushroomCow, CowModel<MushroomCow>> {
    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel<MushroomCow> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initMyceliumRenderLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new MushroomCowMyceliumLayer(this));
    }
}
