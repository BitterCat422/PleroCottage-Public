package net.bittercat.plerocottage.items;

import net.bittercat.plerocottage.blocks.MultiBlockPieBlock;
import net.bittercat.plerocottage.blocks.PlateBlock;
import net.bittercat.plerocottage.blocks.PortionFoodBlock;
import net.bittercat.plerocottage.blocks.StackableFoodBlock;
import net.bittercat.plerocottage.blocks.StackableFoodDisplayBlock;
import net.bittercat.plerocottage.blockentities.PlateBlockEntity;
import net.bittercat.plerocottage.content.food.DisplayFoodContent;
import net.bittercat.plerocottage.content.food.FoodContent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public final class UtensilFoodInteraction {
    private UtensilFoodInteraction() {}

    static InteractionResult eat(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (player == null || context.getHand() != InteractionHand.MAIN_HAND || player.isShiftKeyDown()
            || !player.getAbilities().mayBuild || !level.mayInteract(player, pos)) {
            return InteractionResult.PASS;
        }

        BlockState targetState = level.getBlockState(pos);
        Block targetBlock = targetState.getBlock();
        if (targetBlock instanceof PlateBlock
            && level.getBlockEntity(pos) instanceof PlateBlockEntity plate) {
            return eatFromDisplay(level, plate, player);
        }
        if (targetBlock instanceof CakeBlock || targetBlock instanceof PortionFoodBlock
            || targetBlock instanceof MultiBlockPieBlock) {
            return InteractionResult.PASS;
        }

        ItemStack food = foodStack(targetBlock, player);
        if (food.isEmpty()) {
            return InteractionResult.PASS;
        }
        var foodProperties = food.getFoodProperties(player);
        if (foodProperties == null || !player.canEat(foodProperties.canAlwaysEat())) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!level.setBlockAndUpdate(pos, remainingFood(targetState))) {
            return InteractionResult.FAIL;
        }
        ItemStack remainder = food.finishUsingItem(level, player);
        if (player.getAbilities().instabuild && remainder == food) {
            remainder = food.getItem().getCraftingRemainingItem(food);
        }
        placeOrDropRemainder(context, targetState, remainder);
        return InteractionResult.CONSUME;
    }

    public static InteractionResult eatFromDisplay(Level level, PlateBlockEntity plate, Player player) {
        int slot = lastOccupiedSlot(plate);
        if (slot < 0) {
            return InteractionResult.PASS;
        }

        ItemStack stored = plate.getItem(slot);
        ItemStack food = stored.copyWithCount(1);
        var foodProperties = food.getFoodProperties(player);
        if (foodProperties == null || !player.canEat(foodProperties.canAlwaysEat())) {
            return InteractionResult.PASS;
        }
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        stored.shrink(1);
        if (stored.isEmpty()) {
            plate.setItem(slot, ItemStack.EMPTY);
        } else {
            plate.setChanged();
        }
        ItemStack remainder = food.finishUsingItem(level, player);
        if (player.getAbilities().instabuild && remainder == food) {
            remainder = food.getItem().getCraftingRemainingItem(food);
        }
        if (!remainder.isEmpty()) {
            Block.popResource(level, plate.getBlockPos(), remainder);
        }
        return InteractionResult.CONSUME;
    }

    private static int lastOccupiedSlot(PlateBlockEntity plate) {
        for (int slot = plate.getContainerSize() - 1; slot >= 0; slot--) {
            if (!plate.getItem(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    private static ItemStack foodStack(Block block, Player player) {
        if (block instanceof StackableFoodBlock stack) {
            return new ItemStack(block.asItem());
        }
        if (block instanceof StackableFoodDisplayBlock stack) {
            return new ItemStack(block.asItem());
        }
        ItemStack item = new ItemStack(block.asItem());
        if (!item.isEmpty() && item.getFoodProperties(player) != null) {
            return item;
        }
        return ItemStack.EMPTY;
    }

    private static BlockState remainingFood(BlockState state) {
        IntegerProperty layers = null;
        if (state.getBlock() instanceof StackableFoodBlock) {
            layers = StackableFoodBlock.BLOCKSTATE;
        } else if (state.getBlock() instanceof StackableFoodDisplayBlock) {
            layers = StackableFoodDisplayBlock.BLOCKSTATE;
        }
        if (layers != null && state.getValue(layers) > 0) {
            return state.setValue(layers, state.getValue(layers) - 1);
        }
        return state.getFluidState().createLegacyBlock();
    }

    private static void placeOrDropRemainder(UseOnContext context, BlockState oldState, ItemStack remainder) {
        if (remainder.isEmpty()) {
            return;
        }
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        if (remainder.getItem() instanceof BlockItem blockItem
            && !level.getBlockState(pos).is(oldState.getBlock())) {
            BlockHitResult hit = new BlockHitResult(context.getClickLocation(), context.getClickedFace(), pos,
                context.isInside());
            BlockPlaceContext placementContext = new BlockPlaceContext(context.getPlayer(), context.getHand(),
                remainder, hit);
            if (placementContext.getClickedPos().equals(pos) && placementContext.canPlace()) {
                if (blockItem.place(placementContext).consumesAction()) {
                    BlockState placed = level.getBlockState(pos);
                    BlockState rotated = copyRotation(oldState, placed);
                    if (rotated != placed) {
                        level.setBlock(pos, rotated, 3);
                    }
                    return;
                }
            }
        }
        if (!remainder.isEmpty()) {
            Block.popResource(level, pos, remainder);
        }
    }

    private static BlockState copyRotation(BlockState source, BlockState target) {
        for (var property : target.getProperties()) {
            String name = property.getName();
            if ("facing".equals(name) || "rotation".equals(name) || "axis".equals(name)) {
                var sourceProperty = source.getBlock().getStateDefinition().getProperty(name);
                if (sourceProperty != null) {
                    target = copyRotationValue(target, property, source.getValue(sourceProperty));
                }
            }
        }
        return target;
    }

    private static <T extends Comparable<T>> BlockState copyRotationValue(BlockState target, Property<T> property,
                                                                           Comparable<?> sourceValue) {
        for (T value : property.getPossibleValues()) {
            if (value.equals(sourceValue)) {
                return target.setValue(property, value);
            }
        }
        return target;
    }
}
