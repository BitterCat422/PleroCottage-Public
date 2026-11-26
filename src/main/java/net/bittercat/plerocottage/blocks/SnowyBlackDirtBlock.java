package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class SnowyBlackDirtBlock extends Block {

    public SnowyBlackDirtBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_GRAY)
            .sound(SoundType.ROOTED_DIRT)
            .strength(1.6F, 0.6F)
            .randomTicks()
            .ignitedByLava());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        BlockPos above = pos.above();
        if (level.getBlockState(above).getLightBlock(level, above) > 2) {
            level.setBlock(pos, BlackDirts.BLACK_DIRT.get().defaultBlockState(), 3);
        }
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action,
                                           boolean simulate) {
        if (action == ToolActions.HOE_TILL && context.getItemInHand().canPerformAction(action)) {
            return BlackDirts.RICH_BLACK_DIRT_FARMLAND.get().defaultBlockState();
        }
        return super.getToolModifiedState(state, context, action, simulate);
    }
}
