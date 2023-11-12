package net.merchantpug.bovinesandbuttercups.client.particle;

import net.merchantpug.bovinesandbuttercups.content.particle.ModelLocationParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class ModelLocationParticle extends TextureSheetParticle {
    private final BlockPos pos;
    private final float uo;
    private final float vo;

    public ModelLocationParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ResourceLocation location, String variant) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, location, variant, new BlockPos((int)x, (int)y, (int)z));
    }

    public ModelLocationParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, ResourceLocation location, String variant, BlockPos pos) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.pos = pos;
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
        return this.sprite.getU((this.uo + 1.0F) / 4.0F * 16.0F);
    }

    protected float getU1() {
        return this.sprite.getU(this.uo / 4.0F * 16.0F);
    }

    protected float getV0() {
        return this.sprite.getV(this.vo / 4.0F * 16.0F);
    }

    protected float getV1() {
        return this.sprite.getV((this.vo + 1.0F) / 4.0F * 16.0F);
    }

    public int getLightColor(float p_108291_) {
        int i = super.getLightColor(p_108291_);
        return i == 0 && this.level.hasChunkAt(this.pos) ? LevelRenderer.getLightColor(this.level, this.pos) : i;
    }

    public static class Provider implements ParticleProvider<ModelLocationParticleOption> {
        public Particle createParticle(ModelLocationParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ResourceLocation modelKey = type.modelKey();
            String variant = type.modelVariant();
            return new ModelLocationParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, modelKey, variant);
        }
    }
}
