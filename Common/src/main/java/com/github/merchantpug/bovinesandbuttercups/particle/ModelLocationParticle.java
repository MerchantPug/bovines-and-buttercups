package com.github.merchantpug.bovinesandbuttercups.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ModelLocationParticle extends TextureSheetParticle {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public ModelLocationParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ResourceLocation location, String variant) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, location, variant, new BlockPos(x, y, z));
    }

    public ModelLocationParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ResourceLocation location, String variant, BlockPos p_172459_) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.pos = p_172459_;
        this.setSprite(Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(location, variant)).getParticleIcon());
        this.gravity = 1.0F;
        this.rCol = 0.6F;
        this.gCol = 0.6F;
        this.bCol = 0.6F;
        this.quadSize /= 2.0F;
        this.uo = this.random.nextFloat() * 3.0F;
        this.vo = this.random.nextFloat() * 3.0F;
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected float getU0() {
        return this.sprite.getU((double)((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    protected float getU1() {
        return this.sprite.getU((double)(this.uo / 4.0F * 16.0F));
    }

    protected float getV0() {
        return this.sprite.getV((double)(this.vo / 4.0F * 16.0F));
    }

    protected float getV1() {
        return this.sprite.getV((double)((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    public int getLightColor(float p_108291_) {
        int i = super.getLightColor(p_108291_);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<ModelLocationParticleOption> {
        public Particle createParticle(ModelLocationParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ResourceLocation modelKey = type.getModelKey();
            String variant = type.getModelVariant();
            return new ModelLocationParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, modelKey, variant);
        }
    }
}
