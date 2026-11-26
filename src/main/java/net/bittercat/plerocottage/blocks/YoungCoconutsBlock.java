package net.bittercat.plerocottage.blocks;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blockentities.YoungCoconutsBlockEntity;
import net.bittercat.plerocottage.content.plants.Coconuts;
import net.bittercat.plerocottage.content.plants.Trees;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class YoungCoconutsBlock extends BaseEntityBlock implements BonemealableBlock {

    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 3);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, 1);

    public static final int RIPE = 3;

    private static final float NORMAL_GROW_CHANCE = 0.25F;
    private static final float COLD_GROW_CHANCE = 0.15F;
    private static final float BONEMEAL_GROW_CHANCE = 0.45F;

    private static final VoxelShape SHAPE = Block.box(1, 1, 1, 15, 15, 15);

    public YoungCoconutsBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .sound(SoundType.BAMBOO)
            .strength(0.2F)
            .noOcclusion()
            .randomTicks()
            .isRedstoneConductor((state, level, pos) -> false));
        registerDefaultState(stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
            .setValue(BLOCKSTATE, 0)
            .setValue(ANIMATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BLOCKSTATE, ANIMATION);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new YoungCoconutsBlockEntity(pos, state);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(Trees.COCONUT_TREE.leaves().get());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !canSurvive(state, level, pos)) {
            if (level instanceof ServerLevel serverLevel) {
                Block.dropResources(state, serverLevel, pos, null);
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Coconuts.COCONUT_BLOSSOM.get());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }

        int stage = state.getValue(BLOCKSTATE);
        if (stage < RIPE) {
            float growthChance = level.getBiome(pos).value().getBaseTemperature() < 0.8F
                ? COLD_GROW_CHANCE
                : NORMAL_GROW_CHANCE;
            if (random.nextFloat() < growthChance) {
                level.setBlock(pos, state.setValue(BLOCKSTATE, stage + 1), 3);
            }
            return;
        }

        BlockPos below = pos.below();
        boolean firstFree = level.getBlockState(below).canBeReplaced();
        boolean secondFree = level.getBlockState(below.below()).canBeReplaced();
        if (!firstFree && !secondFree) {
            return;
        }
        level.setBlock(pos, state.setValue(BLOCKSTATE, 0), 3);
        BlockState fallen = Coconuts.YOUNG_COCONUT.get().defaultBlockState();
        if (firstFree) {
            FallingBlockEntity.fall(level, below, fallen);
        }
        if (secondFree) {
            FallingBlockEntity.fall(level, below.below(), fallen);
        }
        level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(BLOCKSTATE) < RIPE && canSurvive(state, level, pos);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < BONEMEAL_GROW_CHANCE;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(BLOCKSTATE, state.getValue(BLOCKSTATE) + 1), 3);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(BLOCKSTATE) != RIPE) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            level.setBlock(pos, state.setValue(BLOCKSTATE, 0), 3);
            ItemStack harvest = new ItemStack(Coconuts.YOUNG_COCONUT_ITEM.get(), 2);
            if (!player.getInventory().add(harvest)) {
                ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, harvest);
                drop.setPickUpDelay(10);
                level.addFreshEntity(drop);
            }
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
