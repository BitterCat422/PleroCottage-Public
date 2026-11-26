package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.Bananas;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BananaFlowerBlock extends Block implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    public static final int MAX_AGE = 2;

    private static final float BONEMEAL_CHANCE = 0.33F;

    private static final VoxelShape SHAPE = Block.box(5, 2, 3, 11, 16, 9);

    public BananaFlowerBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .sound(SoundType.GRASS)
            .strength(0.3F)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
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
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return null;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean clientSide) {
        BlockState above = level.getBlockState(pos.above());
        boolean room = above.canBeReplaced()
            || (above.is(Bananas.TREE_BUNCH.get()) && stageAt(level, pos.above()) < BananaTreeBunchBlock.RIPE);
        return room && state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (random.nextFloat() >= BONEMEAL_CHANCE) {
            return;
        }
        BlockPos bunchPos = pos.above();
        if (level.getBlockState(bunchPos).canBeReplaced()
            && stageAt(level, pos.above(2).north()) < BananaTreeBunchBlock.RIPE) {
            level.setBlock(bunchPos, Bananas.TREE_BUNCH.get().defaultBlockState(), 3);
            return;
        }
        BlockState bunch = level.getBlockState(bunchPos);
        if (!bunch.is(Bananas.TREE_BUNCH.get()) || stageAt(level, bunchPos) >= BananaTreeBunchBlock.RIPE) {
            return;
        }
        level.setBlock(bunchPos, bunch.setValue(BananaTreeBunchBlock.BLOCKSTATE,
            bunch.getValue(BananaTreeBunchBlock.BLOCKSTATE) + 1), 3);
        if (state.getValue(AGE) < MAX_AGE) {
            level.setBlock(pos, state.setValue(AGE, state.getValue(AGE) + 1), 3);
        }
    }

    private static int stageAt(BlockGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.hasProperty(BananaTreeBunchBlock.BLOCKSTATE)
            ? state.getValue(BananaTreeBunchBlock.BLOCKSTATE) : -1;
    }
}
