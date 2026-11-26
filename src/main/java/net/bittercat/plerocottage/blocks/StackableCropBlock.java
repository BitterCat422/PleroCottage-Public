package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SoundType;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.common.ForgeHooks;

public class StackableCropBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    private final Settings settings;

    public StackableCropBlock(Settings settings) {
        this(settings, BlockBehaviour.Properties.of()
            .sound(SoundType.CROP)
            .instabreak()
            .noCollission()
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY));
    }

    public StackableCropBlock(Settings settings, BlockBehaviour.Properties properties) {
        super(properties);
        if (settings.maxAge() > AGE.getPossibleValues().size() - 1) {
            throw new IllegalArgumentException("Stackable crop max age exceeds age property: " + settings.maxAge());
        }
        this.settings = settings;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(TOP, true));
    }

    public Settings settings() {
        return settings;
    }

    public IntegerProperty ageProperty() {
        return AGE;
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, TOP);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(this) || below.canSustainPlant(level, pos.below(), Direction.UP, this);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape[] shapes = state.getValue(TOP) ? settings.topShapes() : settings.stemShapes();
        return shapes[Math.min(state.getValue(AGE), shapes.length - 1)];
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
        return state.getValue(TOP) && state.getValue(AGE) < settings.maxAge();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (!state.getValue(TOP) || state.getValue(AGE) >= settings.maxAge()) {
            return;
        }
        if (!ForgeHooks.onCropsGrowPre(level, pos, state,
                random.nextFloat() < settings.growthChance().chance(state, level, pos, random))) {
            return;
        }
        grow(level, state, pos);
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }

    protected void grow(ServerLevel level, BlockState state, BlockPos pos) {
        BlockPos above = pos.above();
        if (!level.getBlockState(above).canBeReplaced()) {
            return;
        }
        int age = state.getValue(AGE);
        level.setBlock(pos, state.setValue(TOP, false), 2);
        BlockState grown = defaultBlockState().setValue(AGE, age + 1).setValue(TOP, true);
        level.setBlock(above, grown, 3);
        level.gameEvent(GameEvent.BLOCK_PLACE, above, GameEvent.Context.of(grown));
    }

    @Nullable
    private BlockPos findTop(LevelReader level, BlockPos pos) {
        BlockPos current = pos;
        while (level.getBlockState(current).is(this)) {
            if (level.getBlockState(current).getValue(TOP)) {
                return current;
            }
            current = current.above();
            if (current.getY() > pos.getY() + settings.maxAge()) {
                break;
            }
        }
        return null;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        BlockPos top = findTop(level, pos);
        return top != null && level.getBlockState(top.above()).canBeReplaced();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos top = findTop(level, pos);
        if (top == null) {
            return;
        }
        BlockState topState = level.getBlockState(top);
        BlockPos above = top.above();
        if (!level.getBlockState(above).canBeReplaced()) {
            return;
        }
        int age = Math.min(topState.getValue(AGE) + 1, settings.maxAge());
        level.setBlock(top, topState.setValue(TOP, false), 2);
        BlockState grown = defaultBlockState().setValue(AGE, age).setValue(TOP, true);
        level.setBlock(above, grown, 3);
        level.gameEvent(GameEvent.BLOCK_PLACE, above, GameEvent.Context.of(grown));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(settings.seed().get());
    }

    public record Settings(Supplier<Item> seed, int maxAge, GrowthChance growthChance,
                           VoxelShape[] stemShapes, VoxelShape[] topShapes) {

        public Settings(Supplier<Item> seed, int maxAge, float growthChance,
                        VoxelShape[] stemShapes, VoxelShape[] topShapes) {
            this(seed, maxAge, GrowthChance.constant(growthChance), stemShapes, topShapes);
        }
    }
}
