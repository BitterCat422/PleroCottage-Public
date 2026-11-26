package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class VineStakeBlock extends Block implements BonemealableBlock {

    public static final int MAX_HEIGHT = 7;

    private static final float GROW_CHANCE = 0.33F;

    public static final EnumProperty<VineSpecies> SPECIES = EnumProperty.create("species", VineSpecies.class);

    public static final BooleanProperty CLIMBED = BooleanProperty.create("climbed");

    public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 0, MAX_HEIGHT);

    private static final VoxelShape SHAPE = Block.box(6, 0, 6, 10, 16, 10);

    private final Supplier<? extends Block> arbor;

    public VineStakeBlock(Supplier<? extends Block> arbor) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.WOOD)
            .strength(2.0F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        this.arbor = arbor;
        registerDefaultState(stateDefinition.any()
            .setValue(SPECIES, VineSpecies.EMPTY)
            .setValue(CLIMBED, false)
            .setValue(HEIGHT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPECIES, CLIMBED, HEIGHT);
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

    public static boolean isClimbed(BlockState state) {
        return state.getBlock() instanceof VineStakeBlock
            && state.getValue(SPECIES) != VineSpecies.EMPTY
            && state.getValue(CLIMBED);
    }

    private static boolean canGrow(BlockState state) {
        return state.getValue(SPECIES) != VineSpecies.EMPTY
            && !state.getValue(CLIMBED)
            && state.getValue(HEIGHT) < MAX_HEIGHT;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos,
                                boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        update(level, pos, state);
    }

    private void update(Level level, BlockPos pos, BlockState state) {
        BlockState below = level.getBlockState(pos.below());
        if (!isClimbed(below) && !below.is(BlockTags.DIRT)) {
            level.setBlock(pos, defaultBlockState(), 3);
            return;
        }
        if (!state.getValue(CLIMBED)) {
            return;
        }
        BlockState above = level.getBlockState(pos.above());
        boolean carriesOn = above.is(this)
            ? above.getValue(SPECIES) != VineSpecies.EMPTY
            : above.is(arbor.get());
        if (!carriesOn) {
            level.setBlock(pos, state.setValue(CLIMBED, false), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return canGrow(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!canGrow(state) || !ForgeHooks.onCropsGrowPre(level, pos, state, random.nextFloat() < GROW_CHANCE)) {
            return;
        }
        grow(level, pos, state);
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    private void grow(ServerLevel level, BlockPos pos, BlockState state) {
        BlockState above = level.getBlockState(pos.above());
        if (above.is(this) && above.getValue(SPECIES) == VineSpecies.EMPTY) {
            level.setBlock(pos.above(), above
                .setValue(SPECIES, state.getValue(SPECIES))
                .setValue(HEIGHT, state.getValue(HEIGHT) + 1), 3);
            level.setBlock(pos, state.setValue(CLIMBED, true), 3);
        } else if (above.is(arbor.get()) && above.getValue(VineArborBlock.SPECIES) == VineSpecies.EMPTY) {
            level.setBlock(pos.above(), above.setValue(VineArborBlock.SPECIES, state.getValue(SPECIES)), 3);
            level.setBlock(pos, state.setValue(CLIMBED, true), 3);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return canGrow(state);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        
        randomTick(state, level, pos, random);
    }
}
