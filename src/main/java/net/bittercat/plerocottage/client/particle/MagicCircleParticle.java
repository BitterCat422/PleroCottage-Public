package net.bittercat.plerocottage.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public final class MagicCircleParticle extends TextureSheetParticle {
    private static final int GROW_TICKS = 0;
    private static final int HOLD_TICKS = 20;
    private static final int SHRINK_TICKS = 3;

    private MagicCircleParticle(ClientLevel level, double x, double y, double z, double scale,
                                SpriteSet sprites) {
        super(level, x, y, z);
        pickSprite(sprites);
        lifetime = GROW_TICKS + HOLD_TICKS + SHRINK_TICKS;
        quadSize = (float) scale;
        hasPhysics = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float partialTick) {
        float time = age + partialTick;
        float scale = GROW_TICKS > 0 && time < GROW_TICKS
            ? time / GROW_TICKS
            : (lifetime - time) / SHRINK_TICKS;
        return quadSize * Mth.clamp(scale, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        
        if (++age >= lifetime) {
            remove();
        }
    }

    public static final class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y,
                                       double z, double xSpeed, double ySpeed, double zSpeed) {
            return new MagicCircleParticle(level, x, y, z, xSpeed, sprites);
        }
    }
}
