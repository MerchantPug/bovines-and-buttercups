package net.merchantpug.bovinesandbuttercups.client.particle;

import com.mojang.math.Vector3f;
import net.merchantpug.bovinesandbuttercups.content.particle.SparkleParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class SparkleParticle extends SimpleAnimatedParticle {
    protected SparkleParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet spriteSet, Vector3f color) {
        super(level, x, y, z, spriteSet, 0);
        this.setAlpha(1.0F);
        this.setColor(color.x(), color.y(), color.z());
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = false;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.lifetime = (int)(7.0F / (this.random.nextFloat() * 0.9F + 0.1F));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SparkleParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet $$0) {
            this.sprites = $$0;
        }

        public Particle createParticle(SparkleParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Vector3f color = type.color();
            return new SparkleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, color);
        }
    }
}
