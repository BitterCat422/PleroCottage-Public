package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.ForgeEventFactory;

public class PurpleSugarCaneCropBlock extends BushBlock implements BonemealableBlock {
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    private static final VoxelShape SHAPE = Block.box(6.5, 0, 6.5, 9.5, 16, 9.5);
    private final Supplier<Item> seed;

    public PurpleSugarCaneCropBlock(Supplier<Item> seed) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.BAMBOO)
            .strength(1.0F)
            .noOcclusion()
            .randomTicks()
            .isRedstoneConductor((state, level, pos) -> false)
            .dynamicShape()
            .offsetType(Block.OffsetType.XZ));
        this.seed = seed;
        registerDefaultState(stateDefinition.any().setValue(BlockStateProperties.AGE_7, 0).setValue(TOP, true));
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.AGE_7, TOP);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(this) || mayPlaceOn(below, level, pos.below());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        return SHAPE.move(offset.x, offset.y, offset.z);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        if (!state.canSurvive(level, pos)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (direction == Direction.UP) {
            return state.setValue(TOP, !neighbor.is(this));
        }
        return state;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && ForgeEventFactory.getMobGriefingEvent(level, entity)) {
            level.destroyBlock(pos, true, entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(TOP) && state.getValue(BlockStateProperties.AGE_7) < 7;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1) || !state.getValue(TOP)
            || state.getValue(BlockStateProperties.AGE_7) >= 7) {
            return;
        }
        boolean shouldGrow = random.nextFloat() < 0.33F;
        if (!ForgeHooks.onCropsGrowPre(level, pos, state, shouldGrow)) {
            return;
        }
        if (shouldGrow) {
            grow(level, state, pos, random);
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    private void grow(ServerLevel level, BlockState state, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        if (!level.getBlockState(above).canBeReplaced()) {
            return;
        }
        int age = state.getValue(BlockStateProperties.AGE_7) + 1;
        if (age >= 5 && random.nextFloat() < 0.25F) {
            age = 7;
        }
        level.setBlock(pos, state.setValue(TOP, false), 2);
        BlockState grown = defaultBlockState().setValue(BlockStateProperties.AGE_7, age);
        level.setBlock(above, grown, 3);
        level.gameEvent(GameEvent.BLOCK_PLACE, above, GameEvent.Context.of(grown));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        BlockState above = level.getBlockState(pos.above());
        return above.canBeReplaced() || above.is(this);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        if (!level.getBlockState(above).canBeReplaced()) {
            return;
        }
        int currentAge = state.getValue(BlockStateProperties.AGE_7);
        int age = currentAge < 7 ? currentAge + 1 : 7;
        level.setBlock(pos, state.setValue(TOP, false), 2);
        BlockState grown = defaultBlockState().setValue(BlockStateProperties.AGE_7, age);
        level.setBlock(above, grown, 3);
        level.gameEvent(GameEvent.BLOCK_PLACE, above, GameEvent.Context.of(grown));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(seed.get());
    }
}
