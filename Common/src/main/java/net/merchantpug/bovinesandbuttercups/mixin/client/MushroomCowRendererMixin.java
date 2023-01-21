package net.merchantpug.bovinesandbuttercups.mixin.client;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Locale;

@Mixin(value = MushroomCowRenderer.class, priority = 1001)
public abstract class MushroomCowRendererMixin {
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/MushroomCow;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void bovinesandbuttercups$changeTextureLocation(MushroomCow entity, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation location = Services.COMPONENT.getMushroomCowTypeFromCow(entity).getConfiguration().getCowTexture().orElseGet(() -> new ResourceLocation(BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(entity)).getNamespace(), "textures/entity/mooshroom/" + BovineRegistryUtil.getConfiguredCowTypeKey(Services.COMPONENT.getMushroomCowTypeFromCow(entity)).getPath().toLowerCase(Locale.ROOT) + "_mooshroom.png"));
        if (location.equals(cir.getReturnValue())) return;
        if (BovinesAndButtercupsClient.LOADED_COW_TEXTURES.contains(location)) {
            cir.setReturnValue(location);
        } else {
            cir.setReturnValue(BovinesAndButtercups.asResource("textures/entity/mooshroom/missing_mooshroom.png"));
        }
    }
}
