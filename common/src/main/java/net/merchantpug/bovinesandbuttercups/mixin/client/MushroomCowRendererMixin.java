package net.merchantpug.bovinesandbuttercups.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.client.BovinesAndButtercupsClient;
import net.merchantpug.bovinesandbuttercups.data.entity.MushroomCowConfiguration;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.renderer.entity.MushroomCowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.MushroomCow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;

@Mixin(value = MushroomCowRenderer.class, priority = 1500)
public abstract class MushroomCowRendererMixin {
    @ModifyReturnValue(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/MushroomCow;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"))
    private ResourceLocation bovinesandbuttercups$changeTextureLocation(ResourceLocation original, MushroomCow cow) {
        ConfiguredCowType<MushroomCowConfiguration, ?> type = Services.COMPONENT.getMushroomCowTypeFromCow(cow);
        if (type != null) {
            ResourceLocation location = type.configuration().getSettings().cowTexture().orElseGet(() -> new ResourceLocation(BovineRegistryUtil.getConfiguredCowTypeKey(type).getNamespace(), "textures/entity/bovinesandbuttercups/mooshroom/" + BovineRegistryUtil.getConfiguredCowTypeKey(type).getPath().toLowerCase(Locale.ROOT) + "_mooshroom.png"));
            if (!location.equals(original)) {
                if (BovinesAndButtercupsClient.LOADED_COW_TEXTURES.contains(location)) {
                    return location;
                } else {
                    return BovinesAndButtercups.asResource("textures/entity/bovinesandbuttercups/mooshroom/missing_mooshroom.png");
                }
            }
        }
        return original;
    }
}
