package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.blocks.DragonFruitSliceBlock;
import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.bittercat.plerocottage.content.food.FoodDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class DragonFruitSliceItem extends BlockFoodItem {
    public DragonFruitSliceItem(net.minecraft.world.level.block.Block block, FoodDefinition definition) {
        super(block, definition.itemProperties(), definition.useDuration(), false,
            definition.useAnim(), null, false);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockState clickedState = level.getBlockState(context.getClickedPos());
        if (clickedState.getBlock() instanceof DragonFruitSliceBlock
            && (context.getPlayer() == null || !context.getPlayer().isShiftKeyDown())) {
            if (!level.isClientSide) {
                level.setBlock(context.getClickedPos(), clickedState.cycle(DragonFruitSliceBlock.BLOCKSTATE), 3);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        BlockPlaceContext placeContext = new BlockPlaceContext(context);
        if (!level.getBlockState(pos).canBeReplaced(placeContext)) {
            return InteractionResult.PASS;
        }
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockState state = DragonFruits.SLICE_BLOCK.get().defaultBlockState()
            .setValue(DragonFruitSliceBlock.FACING, facing)
            .setValue(DragonFruitSliceBlock.BLOCKSTATE, level.random.nextInt(6));
        if (!state.canSurvive(level, pos)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state, 3);
            if (context.getPlayer() == null || !context.getPlayer().getAbilities().instabuild) {
                context.getItemInHand().shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
