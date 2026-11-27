package net.bittercat.plerocottage.worldgen;

import com.mojang.serialization.Codec;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
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

    /**
     * 和原版树木一样只占用空气、树叶、树苗和可被树替换的方块（草、花等），
     * 其余位置保留原有方块，否则在建筑旁长大的树会把墙和箱子换成原木、树叶。
     */
    private static final StructureProcessor KEEP_EXISTING_BLOCKS = new StructureProcessor() {
        @Override
        public StructureTemplate.StructureBlockInfo processBlock(
                LevelReader level, BlockPos offset, BlockPos pos,
                StructureTemplate.StructureBlockInfo original, StructureTemplate.StructureBlockInfo current,
                StructurePlaceSettings settings) {
            BlockState existing = level.getBlockState(current.pos());
            boolean replaceable = existing.isAir() || existing.canBeReplaced()
                || existing.is(BlockTags.LEAVES) || existing.is(BlockTags.SAPLINGS)
                || existing.is(BlockTags.REPLACEABLE_BY_TREES);
            return replaceable ? current : null;
        }

        @Override
        protected StructureProcessorType<?> getType() {
            return StructureProcessorType.BLOCK_IGNORE;
        }
    };

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
                config.ignoredBlocks().stream().map(Holder::get).toList()))
            .addProcessor(KEEP_EXISTING_BLOCKS);

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
