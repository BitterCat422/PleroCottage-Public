package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;

public class DragonFruitCropBlock extends Block implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    private static final VoxelShape[] SHAPES = { box(5,1,5,11,9,11), box(4,1,4,12,11,12), box(4,1,4,12,12,12) };
    public DragonFruitCropBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.CROP).strength(0.2F).noCollission().noOcclusion().randomTicks());
        registerDefaultState(stateDefinition.any().setValue(AGE, 0));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) { b.add(AGE); }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) { return SHAPES[s.getValue(AGE)]; }
    @Override public boolean canSurvive(BlockState s, LevelReader l, BlockPos p) { return l.getBlockState(p.below()).getBlock() instanceof DragonFruitCactusBlock; }
    @Override public BlockState updateShape(BlockState s, Direction d, BlockState n, LevelAccessor l, BlockPos p, BlockPos np) { return !s.canSurvive(l,p) ? Blocks.AIR.defaultBlockState() : super.updateShape(s,d,n,l,p,np); }
    @Override public void randomTick(BlockState s, ServerLevel l, BlockPos p, RandomSource r) { if (s.getValue(AGE)<2 && r.nextFloat()<.33F) l.setBlock(p,s.cycle(AGE),2); }
    @Override public boolean isValidBonemealTarget(LevelReader l, BlockPos p, BlockState s, boolean c) { return s.getValue(AGE)<2; }
    @Override public boolean isBonemealSuccess(Level l, RandomSource r, BlockPos p, BlockState s) { return true; }
    @Override public void performBonemeal(ServerLevel l, RandomSource r, BlockPos p, BlockState s) { l.setBlock(p,s.cycle(AGE),2); }
}
