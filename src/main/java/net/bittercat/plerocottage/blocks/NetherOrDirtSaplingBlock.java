package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.data.BlockTagProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class NetherOrDirtSaplingBlock extends SaplingBlock {

    public NetherOrDirtSaplingBlock(AbstractTreeGrower grower, BlockBehaviour.Properties properties) {
        super(grower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return isPlantableGround(state);
    }

    static boolean isPlantableGround(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(BlockTagProvider.NETHER_PLANTABLE);
    }
}
