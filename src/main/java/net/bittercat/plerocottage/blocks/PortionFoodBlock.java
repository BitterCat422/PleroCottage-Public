package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class PortionFoodBlock extends BaseEntityBlock {
    public static final IntegerProperty BITES = IntegerProperty.create("blockstate", 0, 3);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final net.minecraft.tags.TagKey<Item> KNIVES = ItemTags.create(
        new ResourceLocation("forge", "tools/knives"));

    private final Supplier<? extends Item> portionItem;

    public PortionFoodBlock(Supplier<? extends Item> portionItem) {
        super(BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5F).noOcclusion()
            .isRedstoneConductor((state, level, pos) -> false));
        this.portionItem = portionItem;
        registerDefaultState(stateDefinition.any().setValue(BITES, 0).setValue(FACING, Direction.NORTH));
    }

    protected int portionHeight() {
        return 8;
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
    public boolean propagatesSkylightDown(BlockState state, net.minecraft.world.level.BlockGetter level,
                                          BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BITES, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level,
                               BlockPos pos, CollisionContext context) {
        int height = portionHeight();
        return switch (state.getValue(BITES)) {
            case 1 -> switch (state.getValue(FACING)) {
                case NORTH -> Shapes.or(box(0, 0, 8, 16, height, 16), box(0, 0, 0, 8, height, 8));
                case EAST -> Shapes.or(box(0, 0, 0, 8, height, 16), box(8, 0, 0, 16, height, 8));
                case WEST -> Shapes.or(box(8, 0, 0, 16, height, 16), box(0, 0, 8, 8, height, 8));
                default -> Shapes.or(box(0, 0, 0, 16, height, 8), box(8, 0, 8, 16, height, 16));
            };
            case 2 -> switch (state.getValue(FACING)) {
                case NORTH -> box(0, 0, 8, 16, height, 16);
                case EAST -> box(0, 0, 0, 8, height, 16);
                case WEST -> box(8, 0, 0, 16, height, 16);
                default -> box(0, 0, 0, 16, height, 8);
            };
            case 3 -> switch (state.getValue(FACING)) {
                case NORTH -> box(8, 0, 8, 16, height, 16);
                case EAST -> box(0, 0, 8, 8, height, 16);
                case WEST -> box(8, 0, 0, 16, height, 8);
                default -> box(0, 0, 0, 8, height, 8);
            };
            default -> box(0, 0, 0, 16, height, 16);
        };
    }

    private static VoxelShape box(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return Shapes.box(minX / 16.0, minY / 16.0, minZ / 16.0,
            maxX / 16.0, maxY / 16.0, maxZ / 16.0);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.MAIN_HAND && player.getMainHandItem().is(KNIVES)
            && !player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                ItemStack portion = new ItemStack(portionItem.get());
                if (!player.getInventory().add(portion)) {
                    return InteractionResult.PASS;
                }
                advance(state, level, pos);
                level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                    SoundSource.NEUTRAL, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (player.isShiftKeyDown()
            || (!player.getAbilities().instabuild && player.getFoodData().getFoodLevel() >= 20)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack portion = new ItemStack(portionItem.get());
            portion.getItem().finishUsingItem(portion, level, player);
            advance(state, level, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void advance(BlockState state, Level level, BlockPos pos) {
        int bites = state.getValue(BITES);
        level.setBlock(pos, bites >= 3 ? Blocks.AIR.defaultBlockState() : state.setValue(BITES, bites + 1), 3);
    }
}
