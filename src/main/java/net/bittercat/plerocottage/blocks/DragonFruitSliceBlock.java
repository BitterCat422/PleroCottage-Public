package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;

public class DragonFruitSliceBlock extends Block {
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 5);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public DragonFruitSliceBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.BAMBOO).instabreak().noOcclusion());
        registerDefaultState(stateDefinition.any().setValue(BLOCKSTATE, 0).setValue(FACING, Direction.NORTH));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(BLOCKSTATE, FACING); }
    @Override public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext c) {
        return defaultBlockState().setValue(FACING, c.getHorizontalDirection().getOpposite());
    }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, net.minecraft.world.phys.shapes.CollisionContext c) { return box(4.5, 0, 4.5, 11.5, 7, 11.5); }
    @Override public boolean canSurvive(BlockState s, LevelReader l, BlockPos p) {
        return l.getBlockState(p.below()).isFaceSturdy(l, p.below(), Direction.UP);
    }
    @Override public BlockState updateShape(BlockState s, Direction d, BlockState n, LevelAccessor l, BlockPos p, BlockPos np) { return !s.canSurvive(l, p) ? net.minecraft.world.level.block.Blocks.AIR.defaultBlockState() : super.updateShape(s, d, n, l, p, np); }
}

