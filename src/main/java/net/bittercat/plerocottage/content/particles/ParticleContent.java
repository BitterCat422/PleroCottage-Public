package net.bittercat.plerocottage.content.particles;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.init.ParticleTypeRegistry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.RegistryObject;

public final class ParticleContent {
    public static final RegistryObject<SimpleParticleType> TRANSMUTATION_MAGIC_CIRCLE =
        ParticleTypeRegistry.PARTICLE_TYPES.register(
            "transmutation_magic_circle", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> MOMENTARY_METEOR_STARS =
        ParticleTypeRegistry.PARTICLE_TYPES.register(
            "momentary_meteor_stars", () -> new SimpleParticleType(false));

    private static final Map<String, RegistryObject<SimpleParticleType>> LEAF_PARTICLES = new LinkedHashMap<>();

    private ParticleContent() {
    }

    public static void bootstrap() {
        registerLeafParticle("peach");
        registerLeafParticle("pear");
        registerLeafParticle("apricots", "apricot_leaves");
        registerLeafParticle("myriclave", "myriclave_tree_leaves");
    }

    public static void registerLeafParticle(String species) {
        registerLeafParticle(species, species + "_leaves");
    }

    public static void registerLeafParticle(String species, String particleName) {
        LEAF_PARTICLES.put(species, ParticleTypeRegistry.PARTICLE_TYPES.register(
            particleName, () -> new SimpleParticleType(false)));
    }

    @Nullable
    public static Supplier<SimpleParticleType> leafParticleFor(String species) {
        return LEAF_PARTICLES.get(species);
    }

    public static Collection<RegistryObject<SimpleParticleType>> leafParticles() {
        return LEAF_PARTICLES.values();
    }
}
