package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public final class ColoredTerracottaBlock extends Block {
    public ColoredTerracottaBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(1.25F, 4.2F)
            .requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM));
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }
}
