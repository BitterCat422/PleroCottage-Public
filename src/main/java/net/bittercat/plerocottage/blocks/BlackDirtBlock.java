package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.bittercat.plerocottage.content.plants.Trees;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class BlackDirtBlock extends Block {

    private static final int LEAF_SCAN = 1;

    public BlackDirtBlock() {
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
        if (level.getBlockState(pos.above()).is(Blocks.SNOW)) {
            level.setBlock(pos, BlackDirts.SNOWY_BLACK_DIRT.get().defaultBlockState(), 3);
            return;
        }
        if (!level.getBlockState(pos.above()).is(Trees.SCOTS_PINE_TREE.species().trunk().get())) {
            return;
        }
        double chance = Config.blackDirtGrowTruffleChance()
            * (1.0D + Math.min(countNearbyPigs(level, pos), 8) * 0.25D);
        if (random.nextDouble() >= chance) {
            return;
        }
        if (hasLooseScotsPineLeaves(level, pos)) {
            level.setBlock(pos, BlackDirts.BLACK_DIRT_TRUFFLE.get().defaultBlockState(), 3);
        }
    }

    private static boolean hasLooseScotsPineLeaves(Level level, BlockPos pos) {
        BlockPos above = pos.above();
        Block leaves = Trees.SCOTS_PINE_TREE.leaves().get();
        for (BlockPos candidate : BlockPos.betweenClosed(
            above.offset(-LEAF_SCAN, 1, -LEAF_SCAN), above.offset(LEAF_SCAN, 3, LEAF_SCAN))) {
            BlockState leaf = level.getBlockState(candidate);
            if (leaf.is(leaves) && !leaf.getValue(LeavesBlock.PERSISTENT)) {
                return true;
            }
        }
        return false;
    }

    private static int countNearbyPigs(Level level, BlockPos pos) {
        return level.getEntitiesOfClass(Pig.class, new AABB(pos).inflate(8.0D)).size();
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
