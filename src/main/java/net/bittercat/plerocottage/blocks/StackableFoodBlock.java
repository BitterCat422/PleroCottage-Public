package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class StackableFoodBlock extends Block {
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 2);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final String foodName;
    private final boolean requiresSupport;

    public StackableFoodBlock(String foodName, boolean requiresSupport) {
        super(BlockBehaviour.Properties.of().strength(0.2F).noOcclusion().pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        this.foodName = foodName;
        this.requiresSupport = requiresSupport;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
            .setValue(BLOCKSTATE, 0));
    }

    public String foodName() {
        return foodName;
    }

    @Override
    public net.minecraft.world.item.ItemStack getCloneItemStack(BlockState state,
            net.minecraft.world.phys.HitResult target, BlockGetter level, BlockPos pos,
            net.minecraft.world.entity.player.Player player) {
        return new net.minecraft.world.item.ItemStack(
            net.bittercat.plerocottage.content.food.FoodContent.item(foodName).get());
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos,
                                     CollisionContext context) {
        return net.minecraft.world.phys.shapes.Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return StackableFoodShapes.get(foodName, state.getValue(BLOCKSTATE), state.getValue(FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                        CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!requiresSupport) {
            return true;
        }
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  net.minecraft.world.level.LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        return !canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState()
            : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BLOCKSTATE);
    }
}
