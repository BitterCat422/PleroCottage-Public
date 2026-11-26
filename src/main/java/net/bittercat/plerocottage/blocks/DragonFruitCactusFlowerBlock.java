package net.bittercat.plerocottage.blocks;

import net.bittercat.plerocottage.content.plants.DragonFruits;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DragonFruitCactusFlowerBlock extends Block implements BonemealableBlock {
    public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;

    public DragonFruitCactusFlowerBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.GRASS).instabreak().noCollission()
            .noOcclusion().randomTicks());
        registerDefaultState(stateDefinition.any().setValue(PERSISTENT, false));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PERSISTENT);
    }
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos, state.setValue(PERSISTENT, true), 3);
    }
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }
    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        return !state.getValue(PERSISTENT) && level.getBlockState(pos.below()).isAir()
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }
    @Override public VoxelShape getShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) {
        return box(0, 0, 0, 16, 5, 16);
    }
    @Override public VoxelShape getVisualShape(BlockState s, BlockGetter l, BlockPos p, CollisionContext c) {
        return Shapes.empty();
    }
    private boolean onCactus(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).getBlock() instanceof DragonFruitCactusBlock;
    }
    @Override public void randomTick(BlockState s, ServerLevel l, BlockPos p, RandomSource r) {
        if (!s.getValue(PERSISTENT) && onCactus(l, p) && r.nextFloat() < 0.2F) {
            l.setBlock(p, DragonFruits.CROP.get().defaultBlockState(), 3);
        }
    }
    @Override public boolean isValidBonemealTarget(LevelReader l, BlockPos p, BlockState s, boolean c) {
        return !s.getValue(PERSISTENT) && onCactus(l, p);
    }
    @Override public boolean isBonemealSuccess(Level l, RandomSource r, BlockPos p, BlockState s) { return true; }
    @Override public void performBonemeal(ServerLevel l, RandomSource r, BlockPos p, BlockState s) {
        l.setBlock(p, DragonFruits.CROP.get().defaultBlockState(), 3);
    }
}
