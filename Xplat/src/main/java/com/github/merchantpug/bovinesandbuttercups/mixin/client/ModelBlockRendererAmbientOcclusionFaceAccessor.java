package com.github.merchantpug.bovinesandbuttercups.mixin.client;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ModelBlockRenderer.AmbientOcclusionFace.class)
public interface ModelBlockRendererAmbientOcclusionFaceAccessor {
    @Accessor("brightness")
    float[] bovinesandbuttercups$getBrightness();

    @Accessor("lightmap")
    int[] bovinesandbuttercups$getLightmap();
}
