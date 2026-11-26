package net.bittercat.plerocottage.content.worldgen;

import net.bittercat.plerocottage.init.FeatureRegistry;
import net.bittercat.plerocottage.worldgen.StructureTemplateFeature;
import net.bittercat.plerocottage.worldgen.StructureTemplateFeatureConfig;
import net.minecraftforge.registries.RegistryObject;

public final class FeatureContent {
    public static final RegistryObject<StructureTemplateFeature> STRUCTURE_FEATURE =
        FeatureRegistry.FEATURES.register("structure_feature",
            () -> new StructureTemplateFeature(StructureTemplateFeatureConfig.CODEC));

    private FeatureContent() {
    }

    public static void bootstrap() {
    }
}
