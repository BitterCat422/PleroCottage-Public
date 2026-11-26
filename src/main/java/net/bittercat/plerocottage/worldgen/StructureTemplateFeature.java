package net.bittercat.plerocottage.worldgen;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class StructureTemplateFeature extends Feature<StructureTemplateFeatureConfig> {

    public StructureTemplateFeature(Codec<StructureTemplateFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StructureTemplateFeatureConfig> context) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        StructureTemplateFeatureConfig config = context.config();

        StructurePlaceSettings settings = new StructurePlaceSettings()
            .setRotation(config.randomRotation() ? Rotation.getRandom(random) : Rotation.NONE)
            .setMirror(config.randomMirror() ? Mirror.values()[random.nextInt(2)] : Mirror.NONE)
            .setRandom(random)
            .setIgnoreEntities(false)
            .addProcessor(new BlockIgnoreProcessor(
                config.ignoredBlocks().stream().map(Holder::get).toList()));

        StructureTemplateManager templates = level.getLevel().getServer().getStructureManager();
        StructureTemplate template = templates.getOrCreate(config.structure());
        BlockPos placePos = context.origin()
            .offset(StructureTemplate.calculateRelativePosition(settings, new BlockPos(config.offset())));
        boolean placed = template.placeInWorld(level, placePos, placePos, settings, random, 2);
        if (placed) {
            
            config.groundPetals().ifPresent(flower ->
                GroundPetalsDecorator.scatter(level, random, context.origin(), flower,
                    (pos, state) -> level.setBlock(pos, state, 2)));
        }
        return true;
    }
}
