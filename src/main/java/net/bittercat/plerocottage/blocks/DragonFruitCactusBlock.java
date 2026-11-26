package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class DragonFruitCactusBlock extends CactusBlock {
    public DragonFruitCactusBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.4F)
            .noOcclusion().randomTicks().isRedstoneConductor((s, l, p) -> false));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        int height = 1;
        while (level.getBlockState(pos.below(height)).is(this)) height++;
        BlockPos above = pos.above();
        if (height >= 3 && level.isEmptyBlock(above)) {
            int age = state.getValue(AGE);
            if (age >= 15) {
                level.setBlock(above, DragonFruits.CACTUS_FLOWER.get().defaultBlockState(), 3);
                level.setBlock(pos, state.setValue(AGE, 0), 4);
            } else {
                level.setBlock(pos, state.setValue(AGE, age + 1), 4);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos side = pos.relative(direction);
            if (level.getBlockState(side).isSolidRender(level, side)
                || level.getFluidState(side).is(FluidTags.LAVA)) return false;
        }
        BlockState below = level.getBlockState(pos.below());
        return (below.is(this) || below.is(BlockTags.SAND)) && !level.getBlockState(pos.above()).liquid();
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return 0;
    }
}
