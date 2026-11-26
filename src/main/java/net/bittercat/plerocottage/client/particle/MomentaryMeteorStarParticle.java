package net.bittercat.plerocottage.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public final class MomentaryMeteorStarParticle extends TextureSheetParticle {
    private MomentaryMeteorStarParticle(ClientLevel level, double x, double y, double z,
                                        double xSpeed, double ySpeed, double zSpeed,
                                        SpriteSet sprites) {
        super(level, x, y, z);
        pickSprite(sprites);
        xd = xSpeed;
        yd = ySpeed;
        zd = zSpeed;
        quadSize = 0.25F;
        lifetime = 16 + random.nextInt(6);
        hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (age++ >= lifetime) {
            remove();
            return;
        }
        move(xd, yd, zd);
        xd *= 0.96D;
        yd *= 0.96D;
        zd *= 0.96D;
    }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new MomentaryMeteorStarParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites);
        }
    }
}
