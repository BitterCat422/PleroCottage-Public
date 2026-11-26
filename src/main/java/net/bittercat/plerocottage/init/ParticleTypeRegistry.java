package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.particles.SimpleParticleType;

public final class ParticleTypeRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, PleroCottage.MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE = PARTICLE_TYPES;
    public static final RegistryObject<SimpleParticleType> COSY_STEAM =
        PARTICLE_TYPES.register("cosy_steam", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> OIL_SPLASH =
        PARTICLE_TYPES.register("oil_splash", () -> new SimpleParticleType(false));

    private ParticleTypeRegistry() {
    }
}
