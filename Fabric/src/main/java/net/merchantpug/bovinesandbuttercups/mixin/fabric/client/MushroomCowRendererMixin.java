package net.merchantpug.bovinesandbuttercups.mixin.fabric.client;

import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowDatapackMushroomLayer;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.MushroomCowMyceliumLayer;
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
    public MushroomCowRendererMixin(EntityRendererProvider.Context context, CowModel<MushroomCow> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void bovinesandbuttercups$initRenderLayers(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new MushroomCowDatapackMushroomLayer<>(this, context.getBlockRenderDispatcher()));
        this.addLayer(new MushroomCowMyceliumLayer(this));
    }
}
