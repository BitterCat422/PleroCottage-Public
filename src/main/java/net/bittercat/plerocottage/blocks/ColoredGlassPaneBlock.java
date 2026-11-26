package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

public final class ColoredGlassPaneBlock extends IronBarsBlock {
    public ColoredGlassPaneBlock(MapColor mapColor) {
        super(BlockBehaviour.Properties.of().mapColor(mapColor).sound(SoundType.GLASS).strength(0.3F)
            .noOcclusion().isRedstoneConductor((state, level, pos) -> false)
            .instrument(NoteBlockInstrument.HAT));
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos,
                                             FluidState fluidState) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentState, Direction side) {
        if (side.getAxis().isVertical()) {
            return adjacentState.getBlock() == this;
        }
        return super.skipRendering(state, adjacentState, side);
    }
}
