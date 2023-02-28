package net.merchantpug.bovinesandbuttercups.client.particle;

import net.merchantpug.bovinesandbuttercups.content.particle.ShroomParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.joml.Vector3f;

public class ShroomParticle extends TextureSheetParticle {
    protected ShroomParticle(ClientLevel level, SpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd, Vector3f color) {
        super(level, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.pickSprite(spriteSet);
        this.setColor(color.x(), color.y(), color.z());
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.6F;
        this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.hasPhysics = false;
        this.friction = 1.0F;
        this.gravity = 0.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<ShroomParticleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(ShroomParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Vector3f color = type.color();
            return new ShroomParticle(level, spriteSet, x, y, z, xSpeed, ySpeed, zSpeed, color);
        }
    }
}
