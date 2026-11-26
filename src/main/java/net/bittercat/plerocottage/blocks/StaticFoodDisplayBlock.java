package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.blockentities.StaticFoodDisplayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.HitResult;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class StaticFoodDisplayBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private final VoxelShape shape;
    private final boolean requiresSupport;
    private final boolean rotatable;
    private final boolean waterloggable;
    private final String shapeName;
    private final Supplier<? extends Item> pickItem;

    public StaticFoodDisplayBlock(boolean facing, boolean waterlogged) {
        this(facing, waterlogged, Shapes.block(), false);
    }

    public StaticFoodDisplayBlock(boolean facing, boolean waterlogged, VoxelShape shape) {
        this(facing, waterlogged, shape, false);
    }

    public StaticFoodDisplayBlock(boolean facing, boolean waterlogged, VoxelShape shape,
                                  boolean requiresSupport) {
        this(facing, waterlogged, shape, requiresSupport, null);
    }

    public StaticFoodDisplayBlock(boolean facing, boolean waterlogged, VoxelShape shape,
                                  boolean requiresSupport, String shapeName) {
        this(facing, waterlogged, shape, requiresSupport, shapeName, null);
    }

    public StaticFoodDisplayBlock(boolean facing, boolean waterlogged, VoxelShape shape,
                                  boolean requiresSupport, String shapeName,
                                  Supplier<? extends Item> pickItem) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.2F).noOcclusion()
            .isRedstoneConductor((state, level, pos) -> false));
        this.shape = shape;
        this.requiresSupport = requiresSupport;
        this.rotatable = facing;
        this.waterloggable = waterlogged;
        this.shapeName = shapeName;
        this.pickItem = pickItem;
        BlockState defaultState = stateDefinition.any();
        if (rotatable) {
            defaultState = defaultState.setValue(FACING, Direction.NORTH);
        }
        if (waterloggable) {
            defaultState = defaultState.setValue(WATERLOGGED, false);
        }
        registerDefaultState(defaultState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StaticFoodDisplayBlockEntity(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level,
                                       BlockPos pos, Player player) {
        return pickItem == null ? super.getCloneItemStack(state, target, level, pos, player)
            : new ItemStack(pickItem.get());
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
        return rotatable && shapeName != null && state.hasProperty(FACING)
            ? FoodCollisionShapes.get(shapeName, state.getValue(FACING)) : shape;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !requiresSupport || hasSupport(level, pos);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (waterloggable && state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return requiresSupport && !hasSupport(level, pos) ? Blocks.AIR.defaultBlockState()
            : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        if (rotatable) {
            state = state.setValue(FACING, context.getHorizontalDirection().getOpposite());
        }
        if (waterloggable) {
            state = state.setValue(WATERLOGGED,
                context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
        }
        return state;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return waterloggable && state.getValue(WATERLOGGED)
            ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private static boolean hasSupport(BlockGetter level, BlockPos pos) {
        BlockPos supportPos = pos.below();
        return level.getBlockState(supportPos).isFaceSturdy(level, supportPos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }
}
