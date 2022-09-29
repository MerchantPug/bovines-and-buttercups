package net.merchantpug.bovinesandbuttercups.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.ConfiguredCowTypeRegistryUtil;
import net.merchantpug.bovinesandbuttercups.platform.Services;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomCowMushroomLayer.class)
public class MushroomCowMushroomLayerMixin<T extends MushroomCow> {
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/animal/MushroomCow;FFFFFF)V", at = @At("HEAD"), cancellable = true)
    private void bovinesandbuttercups$cancelMushroomRenderIfNotDefault(PoseStack stack, MultiBufferSource bufferSource, int light, T entity, float f, float g, float h, float i, float j, float k, CallbackInfo ci) {
        if (!ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.PLATFORM.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("red_mushroom")) && !ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.PLATFORM.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("brown_mushroom"))
                || (ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.PLATFORM.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("red_mushroom"))
                && (Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().mushroom().blockState().isEmpty() || !Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().mushroom().blockState().get().is(Blocks.RED_MUSHROOM)))
                || (ConfiguredCowTypeRegistryUtil.getConfiguredCowTypeKey(entity.level, Services.PLATFORM.getMushroomCowTypeFromCow(entity)).equals(BovinesAndButtercups.asResource("brown_mushroom"))
                && (Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().mushroom().blockState().isEmpty() || !Services.PLATFORM.getMushroomCowTypeFromCow(entity).getConfiguration().mushroom().blockState().get().is(Blocks.BROWN_MUSHROOM)))) {
            ci.cancel();
        }
    }
}
