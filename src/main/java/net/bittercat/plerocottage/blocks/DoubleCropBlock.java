package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.ForgeEventFactory;

public class DoubleCropBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    private final Settings settings;

    public DoubleCropBlock(Settings settings) {
        this(settings, BlockBehaviour.Properties.of()
            .sound(net.minecraft.world.level.block.SoundType.CROP)
            .instabreak()
            .noCollission()
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY));
    }

    public DoubleCropBlock(Settings settings, BlockBehaviour.Properties properties) {
        super(properties);
        this.settings = settings;
    }

    public Settings settings() {
        return settings;
    }

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return PlantType.CROP;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        return false;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return super.canSurvive(state, level, pos);
        }
        BlockPos below = pos.below();
        boolean soil = mayPlaceOn(level.getBlockState(below), level, below);
        if (state.getBlock() == this) {
            soil = level.getBlockState(below).canSustainPlant(level, below, Direction.UP, this);
        }
        return soil
            && hasEnoughLight(level, pos)
            && (state.getValue(AGE) < settings.doubleAge() || isUpper(level.getBlockState(pos.above())));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            VoxelShape[] upper = settings.upperShapes();
            int index = Math.min(Math.max(0, state.getValue(AGE) - settings.doubleAge()), upper.length - 1);
            return upper[index];
        }
        VoxelShape[] lower = settings.lowerShapes();
        return lower[Math.min(state.getValue(AGE), lower.length - 1)];
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && ForgeEventFactory.getMobGriefingEvent(level, entity)) {
            level.destroyBlock(pos, true, entity);
        }
        super.entityInside(state, level, pos, entity);
    }

    public int getHarvestAge() {
        return settings.doubleAge() + 1;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE) < settings.maxAge()) {
            return InteractionResult.PASS;
        }

        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.LOWER ? pos : pos.below();
        BlockPos upperPos = lowerPos.above();
        BlockState lowerState = level.getBlockState(lowerPos);
        if (!lowerState.is(this) || lowerState.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            // 采摘只给收获物，不走战利品表：否则"没熟也掉一份种子"的那一池会在采摘时跟着掉出来。
            int count = Mth.nextInt(level.random, settings.harvestMin(), settings.harvestMax());
            Block.popResource(level, upperPos, new ItemStack(settings.harvest().get(), count));

            level.setBlock(lowerPos, lowerState.setValue(AGE, getHarvestAge()), 2);
            level.setBlock(upperPos, level.getBlockState(upperPos).setValue(AGE, getHarvestAge()), 3);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= settings.maxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        float speed = CropGrowth.speed(this, level, pos);
        if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
            grow(level, state, pos, 1);
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    protected void grow(ServerLevel level, BlockState state, BlockPos pos, int amount) {
        int newAge = Math.min(state.getValue(AGE) + amount, settings.maxAge());
        if (!canGrow(level, pos, state, newAge)) {
            return;
        }
        level.setBlock(pos, state.setValue(AGE, newAge), 2);
        if (newAge >= settings.doubleAge()) {
            BlockPos above = pos.above();
            level.setBlock(above, copyWaterloggedFrom(level, above,
                defaultBlockState().setValue(AGE, newAge).setValue(HALF, DoubleBlockHalf.UPPER)), 3);
        }
    }

    private boolean canGrow(LevelReader level, BlockPos pos, BlockState state, int newAge) {
        return !isMaxAge(state)
            && hasEnoughLight(level, pos)
            && (newAge < settings.doubleAge() || canReplace(level, pos.above()));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        PosAndState lower = getLowerHalf(level, pos, state);
        return lower != null && canGrow(level, lower.pos(), lower.state(), lower.state().getValue(AGE) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        PosAndState lower = getLowerHalf(level, pos, state);
        if (lower != null) {
            grow(level, lower.state(), lower.pos(), 1);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(settings.seed().get());
    }

    private static boolean hasEnoughLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
    }

    private boolean canReplace(LevelReader level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.isAir() || state.is(this);
    }

    private boolean isUpper(BlockState state) {
        return state.is(this) && state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    @Nullable
    private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
        if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return new PosAndState(pos, state);
        }
        BlockState below = level.getBlockState(pos.below());
        return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER
            ? new PosAndState(pos.below(), below) : null;
    }

    private record PosAndState(BlockPos pos, BlockState state) {
    }

    public record Settings(VoxelShape[] lowerShapes, VoxelShape[] upperShapes, Supplier<Item> seed,
                           Supplier<Item> harvest, int harvestMin, int harvestMax, int maxAge, int doubleAge) {
    }
}
