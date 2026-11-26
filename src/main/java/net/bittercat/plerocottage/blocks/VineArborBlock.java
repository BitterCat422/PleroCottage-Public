package net.bittercat.plerocottage.blocks;

import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

public class VineArborBlock extends Block implements BonemealableBlock {

    public static final int MAX_SPREAD = 2;

    private static final float SPREAD_CHANCE = 0.33F;

    private static final float CROP_CHANCE = 0.35F;

    public static final EnumProperty<VineSpecies> SPECIES = EnumProperty.create("species", VineSpecies.class);

    public static final IntegerProperty SPREAD = IntegerProperty.create("spread", 0, MAX_SPREAD);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    private static final Direction[] HORIZONTALS = {
        Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST
    };

    private final Supplier<? extends Block> stake;

    private final Map<VineSpecies, Supplier<? extends Block>> crops;

    public VineArborBlock(Supplier<? extends Block> stake, Map<VineSpecies, Supplier<? extends Block>> crops) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.WOOD)
            .strength(2.0F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        this.stake = stake;
        this.crops = Map.copyOf(crops);
        registerDefaultState(stateDefinition.any()
            .setValue(SPECIES, VineSpecies.EMPTY)
            .setValue(SPREAD, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SPECIES, SPREAD);
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
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos,
                                boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        update(level, pos, state);
    }

    private void update(Level level, BlockPos pos, BlockState state) {
        int spread = state.getValue(SPREAD);
        if (spread == 0) {
            if (!VineStakeBlock.isClimbed(level.getBlockState(pos.below()))) {
                level.setBlock(pos, defaultBlockState(), 3);
            }
            return;
        }
        boolean anchored = false;
        boolean carrying = false;
        for (Direction direction : HORIZONTALS) {
            BlockState neighbor = level.getBlockState(pos.relative(direction));
            if (!neighbor.is(this)) {
                continue;
            }
            if (neighbor.getValue(SPREAD) == spread - 1) {
                anchored = true;
            }
            if (neighbor.getValue(SPECIES) == state.getValue(SPECIES)) {
                carrying = true;
            }
        }
        if (!anchored || !carrying) {
            level.setBlock(pos, defaultBlockState(), 3);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(SPECIES) != VineSpecies.EMPTY;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        VineSpecies species = state.getValue(SPECIES);
        if (species == VineSpecies.EMPTY) {
            return;
        }
        
        boolean spreads = state.getValue(SPREAD) < MAX_SPREAD && random.nextFloat() < SPREAD_CHANCE;
        boolean fruits = random.nextFloat() < CROP_CHANCE;
        if (!ForgeHooks.onCropsGrowPre(level, pos, state, spreads || fruits)) {
            return;
        }
        if (spreads) {
            spread(level, pos, species, state.getValue(SPREAD));
        }
        if (fruits) {
            growCrop(level, pos, random, species);
        }
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    private void spread(ServerLevel level, BlockPos pos, VineSpecies species, int spread) {
        for (Direction direction : HORIZONTALS) {
            BlockPos neighborPos = pos.relative(direction);
            BlockState neighbor = level.getBlockState(neighborPos);
            if (neighbor.is(this) && neighbor.getValue(SPECIES) == VineSpecies.EMPTY) {
                level.setBlock(neighborPos,
                    neighbor.setValue(SPECIES, species).setValue(SPREAD, spread + 1), 3);
            }
        }
    }

    private void growCrop(ServerLevel level, BlockPos pos, RandomSource random, VineSpecies species) {
        BlockPos cropPos = pos.below();
        BlockState crop = level.getBlockState(cropPos);
        Block expected = crops.get(species).get();
        if (crop.canBeReplaced()) {
            level.setBlock(cropPos, expected.defaultBlockState(), 3);
        } else if (crop.is(expected) && crop.getBlock() instanceof VineCropBlock vineCrop) {
            vineCrop.advance(level, cropPos, crop, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(SPECIES) != VineSpecies.EMPTY;
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
