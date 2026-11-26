package net.bittercat.plerocottage.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class FallingLeafParticle extends TextureSheetParticle {

    private static final float FALL_ACCELERATION = 7.5E-4F;
    
    private static final float WIND_SCALE = 2.0F;
    
    private static final double WIND_ACCELERATION = 0.0025D;
    
    private static final float INITIAL_ROTATION_SPEED = 30.0F;
    
    private static final float SPIN_ACCELERATION = 5.0F;
    
    private static final int LIFETIME = 300;

    private float rotationSpeed;

    private final float windAngle;
    private final float spinAcceleration;

    protected FallingLeafParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        setSprite(sprites.get(random));
        rotationSpeed = (float) Math.toRadians(
            random.nextBoolean() ? -INITIAL_ROTATION_SPEED : INITIAL_ROTATION_SPEED);
        windAngle = random.nextFloat();
        spinAcceleration = (float) Math.toRadians(
            random.nextBoolean() ? -SPIN_ACCELERATION : SPIN_ACCELERATION);
        lifetime = LIFETIME;
        gravity = FALL_ACCELERATION;
        float size = random.nextBoolean() ? 0.05F : 0.075F;
        quadSize = size;
        setSize(size, size);
        friction = 1.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        xo = x;
        yo = y;
        zo = z;
        if (lifetime-- <= 0) {
            remove();
        }
        if (removed) {
            return;
        }
        
        float progress = Math.min((float) (LIFETIME - lifetime) / LIFETIME, 1.0F);
        double windX = Math.cos(Math.toRadians(windAngle * 60.0F)) * WIND_SCALE * Math.pow(progress, 1.25D);
        double windZ = Math.sin(Math.toRadians(windAngle * 60.0F)) * WIND_SCALE * Math.pow(progress, 1.25D);
        xd += windX * WIND_ACCELERATION;
        zd += windZ * WIND_ACCELERATION;
        yd -= gravity;
        rotationSpeed += spinAcceleration / 20.0F;
        oRoll = roll;
        roll += rotationSpeed / 20.0F;
        move(xd, yd, zd);
        if (onGround || (lifetime < LIFETIME - 1 && xd == 0.0D && zd == 0.0D)) {
            remove();
        }
        if (removed) {
            return;
        }
        xd *= friction;
        yd *= friction;
        zd *= friction;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new FallingLeafParticle(level, x, y, z, sprites);
        }
    }
}
