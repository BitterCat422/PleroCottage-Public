package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public final class ColoredConcretePowderBlock extends ConcretePowderBlock {
    public ColoredConcretePowderBlock(Block concrete) {
        super(concrete, BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).sound(SoundType.SNOW)
            .strength(0.5F).instrument(NoteBlockInstrument.SNARE));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }
}
