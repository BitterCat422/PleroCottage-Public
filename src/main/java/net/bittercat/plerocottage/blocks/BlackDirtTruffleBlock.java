package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.BlackDirts;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class BlackDirtTruffleBlock extends Block {

    private static final int MIN_TRUFFLES = 1;
    private static final int MAX_TRUFFLES = 3;

    public BlackDirtTruffleBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.TERRACOTTA_GRAY)
            .sound(SoundType.ROOTED_DIRT)
            .strength(1.6F, 0.6F)
            .ignitedByLava());
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction action,
                                           boolean simulate) {
        if (action != ToolActions.HOE_TILL || !context.getItemInHand().canPerformAction(action)) {
            return super.getToolModifiedState(state, context, action, simulate);
        }
        if (!simulate && context.getLevel() instanceof ServerLevel serverLevel) {
            BlockPos pos = context.getClickedPos();
            ItemEntity drop = new ItemEntity(serverLevel, pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D,
                new ItemStack(BlackDirts.TRUFFLE.get(), Mth.nextInt(serverLevel.random, MIN_TRUFFLES, MAX_TRUFFLES)));
            drop.setPickUpDelay(10);
            serverLevel.addFreshEntity(drop);
        }
        return BlackDirts.BLACK_DIRT.get().defaultBlockState();
    }
}
