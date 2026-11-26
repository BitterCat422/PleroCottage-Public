package net.bittercat.plerocottage.init;

import net.bittercat.plerocottage.PleroCottage;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class FeatureRegistry {

    public static final DeferredRegister<Feature<?>> FEATURES =
        DeferredRegister.create(ForgeRegistries.FEATURES, PleroCottage.MODID);

    private FeatureRegistry() {
    }
}
