package net.bittercat.plerocottage.items;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public final class StackableFoodItem extends ItemNameBlockItem {
    private final int useDuration;
    private final int maximumState;

    public StackableFoodItem(Item.Properties properties, int useDuration,
                             Supplier<? extends Block> block, int maximumState) {
        super(block.get(), properties);
        this.useDuration = useDuration;
        this.maximumState = maximumState;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return useDuration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Direction direction = context.getClickedFace();
        BlockState placedState = getBlock().defaultBlockState();
        if (placedState.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            placedState = placedState.setValue(BlockStateProperties.HORIZONTAL_FACING,
                player.getDirection().getOpposite());
        }

        if (player.isShiftKeyDown()) {
            if (direction == Direction.DOWN) {
                return InteractionResult.PASS;
            }
            BlockPos targetPos = clickedPos.relative(direction);
            BlockPos supportPos = targetPos.below();
            if (!level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP)
                || !level.getBlockState(targetPos).canBeReplaced()) {
                return InteractionResult.PASS;
            }
            return place(level, targetPos, placedState, context.getItemInHand(), player);
        }

        IntegerProperty property = blockstateProperty(placedState);
        if (property == null) {
            return InteractionResult.PASS;
        }
        BlockPos targetPos = clickedPos;
        BlockState targetState = level.getBlockState(targetPos);
        if (direction == Direction.UP
            && level.getBlockState(clickedPos).isFaceSturdy(level, clickedPos, Direction.UP)) {
            BlockPos above = clickedPos.above();
            BlockState aboveState = level.getBlockState(above);
            if (aboveState.is(getBlock()) && canIncrease(aboveState, property)) {
                targetPos = above;
                targetState = aboveState;
            }
        }
        if (targetState == level.getBlockState(clickedPos)
            && (!targetState.is(getBlock())
                || !level.getBlockState(targetPos.below()).isFaceSturdy(level, targetPos.below(), Direction.UP)
                || !canIncrease(targetState, property))) {
            return InteractionResult.PASS;
        }

        BlockState increased = targetState.setValue(property, targetState.getValue(property) + 1);
        return place(level, targetPos, increased, context.getItemInHand(), player);
    }

    private boolean canIncrease(BlockState state, IntegerProperty property) {
        return state.hasProperty(property) && state.getValue(property) < maximumState;
    }

    private static IntegerProperty blockstateProperty(BlockState state) {
        return state.hasProperty(net.bittercat.plerocottage.blocks.StackableFoodBlock.BLOCKSTATE)
            ? net.bittercat.plerocottage.blocks.StackableFoodBlock.BLOCKSTATE
            : state.hasProperty(net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock.BLOCKSTATE)
                ? net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock.BLOCKSTATE : null;
    }

    private static InteractionResult place(Level level, BlockPos pos, BlockState state,
                                           ItemStack stack, Player player) {
        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            level.playSound(null, pos, state.getSoundType().getPlaceSound(),
                SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
