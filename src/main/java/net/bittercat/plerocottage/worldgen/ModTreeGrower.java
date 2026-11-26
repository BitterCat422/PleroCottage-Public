package net.bittercat.plerocottage.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModTreeGrower extends AbstractTreeGrower {

    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public ModTreeGrower(String name) {
        this(ResourceKey.create(Registries.CONFIGURED_FEATURE,
            new ResourceLocation(net.bittercat.plerocottage.PleroCottage.MODID, name + "_tree")));
    }

    public ModTreeGrower(ResourceKey<ConfiguredFeature<?, ?>> feature) {
        this.feature = feature;
    }

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        return feature;
    }
}
