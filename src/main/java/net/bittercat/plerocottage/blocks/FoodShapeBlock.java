package net.bittercat.plerocottage.blocks;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import java.util.function.Supplier;

public final class FoodShapeBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final VoxelShape shape;
    private final boolean rotatable;
    private final boolean waterloggable;
    private final String shapeName;
    private final Supplier<? extends Item> pickItem;

    public FoodShapeBlock(Properties properties, VoxelShape shape, boolean rotatable, boolean waterloggable,
                          String shapeName, Supplier<? extends Item> pickItem) {
        super(properties);
        this.shape = shape;
        this.rotatable = rotatable;
        this.waterloggable = waterloggable;
        this.shapeName = shapeName;
        this.pickItem = pickItem;
        BlockState state = stateDefinition.any();
        if (rotatable) state = state.setValue(FACING, Direction.NORTH);
        if (waterloggable) state = state.setValue(WATERLOGGED, false);
        registerDefaultState(state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return rotatedShape(state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos,
                                        CollisionContext context) {
        return rotatedShape(state);
    }

    private VoxelShape rotatedShape(BlockState state) {
        return rotatable && state.hasProperty(FACING)
            ? FoodCollisionShapes.get(shapeName, state.getValue(FACING)) : shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        if (rotatable) state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
        if (waterloggable) state = state.setValue(WATERLOGGED,
            context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return waterloggable && state.getValue(WATERLOGGED)
            ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level,
                                       BlockPos pos, Player player) {
        return new ItemStack(pickItem.get());
    }
}
