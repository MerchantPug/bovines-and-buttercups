package net.merchantpug.bovinesandbuttercups.client.particle;

import com.mojang.math.Vector3f;
import net.merchantpug.bovinesandbuttercups.content.particle.BloomParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class BloomParticle extends SimpleAnimatedParticle {
    protected BloomParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet, Vector3f color) {
        super(level, x, y, z, spriteSet, 0);
        this.setAlpha(1.0F);
        this.setColor(color.x(), color.y(), color.z());
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = false;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = (int)(16.0D / (Math.random() * 0.8D + 0.2D));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<BloomParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(BloomParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Vector3f color = type.color();
            return new BloomParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, color);
        }
    }
}
