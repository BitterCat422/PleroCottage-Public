package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MultiBlockPieBlock extends BaseEntityBlock {
    public static final IntegerProperty PART = IntegerProperty.create("blockstate", 1, 4);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final Supplier<? extends Item> sliceItem;

    public MultiBlockPieBlock(Supplier<? extends Item> sliceItem) {
        super(BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.2F).noOcclusion()
            .isRedstoneConductor((state, level, pos) -> false));
        this.sliceItem = sliceItem;
        registerDefaultState(stateDefinition.any().setValue(PART, 3)
            .setValue(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new net.bittercat.plerocottage.blockentities.StaticFoodDisplayBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        int part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        int shapePart = switch (part) {
            case 1 -> 2;
            case 2 -> 1;
            default -> part;
        };
        return switch (shapePart) {
            case 1 -> switch (facing) {
                case NORTH -> box(3, 0, 3, 16, 6, 16);
                case EAST -> box(0, 0, 3, 13, 6, 16);
                case WEST -> box(3, 0, 0, 16, 6, 13);
                default -> box(0, 0, 0, 13, 6, 13);
            };
            
            case 2 -> switch (facing) {
                case NORTH -> box(0, 0, 3, 13, 6, 16);
                case EAST -> box(0, 0, 0, 13, 6, 13);
                case WEST -> box(3, 0, 3, 16, 6, 16);
                default -> box(3, 0, 0, 16, 6, 13);
            };
            
            case 3 -> switch (facing) {
                case NORTH -> box(3, 0, 0, 16, 6, 13);
                case EAST -> box(3, 0, 3, 16, 6, 16);
                case WEST -> box(0, 0, 0, 13, 6, 13);
                default -> box(0, 0, 3, 13, 6, 16);
            };
            
            case 4 -> switch (facing) {
                case NORTH -> box(0, 0, 0, 13, 6, 13);
                case EAST -> box(3, 0, 0, 16, 6, 13);
                case WEST -> box(0, 0, 3, 13, 6, 16);
                default -> box(3, 0, 3, 16, 6, 16);
            };
            default -> box(2, 0, 2, 15, 6, 15);
        };
    }

    private static VoxelShape box(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return Shapes.box(minX / 16.0, minY / 16.0, minZ / 16.0,
            maxX / 16.0, maxY / 16.0, maxZ / 16.0);
    }

    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        Direction facing = context.getHorizontalDirection();
        if (!canPlace(level, pos, facing)) {
            return null;
        }
        return defaultBlockState().setValue(PART, 3).setValue(FACING, facing);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            net.minecraft.world.entity.LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        Direction expansionFacing = state.getValue(FACING);
        BlockPos right = pos.relative(expansionFacing.getClockWise());
        BlockPos forward = pos.relative(expansionFacing);
        level.setBlock(pos, state.setValue(PART, 3), 3);
        level.setBlock(right, state.setValue(PART, 4), 3);
        level.setBlock(forward, state.setValue(PART, 2), 3);
        level.setBlock(right.relative(expansionFacing), state.setValue(PART, 1), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (!player.getAbilities().instabuild && player.getFoodData().getFoodLevel() >= 20) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack slice = new ItemStack(sliceItem.get());
            slice.getItem().finishUsingItem(slice, level, player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean hasSupport(Level level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    private static boolean canPlace(Level level, BlockPos origin, Direction facing) {
        BlockPos[] positions = positions(origin, facing);
        for (BlockPos position : positions) {
            if (!hasSupport(level, position)
                || (!position.equals(origin) && !level.getBlockState(position).canBeReplaced())) {
                return false;
            }
        }
        return true;
    }

    private static BlockPos[] positions(BlockPos origin, Direction facing) {
        Direction rightDirection = facing.getClockWise();
        BlockPos right = origin.relative(rightDirection);
        BlockPos forward = origin.relative(facing);
        return new BlockPos[] {origin, right, forward, right.relative(facing)};
    }
}
