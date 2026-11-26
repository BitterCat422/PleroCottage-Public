package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VineCropBlock extends Block implements BonemealableBlock {

    public static final int MAX_AGE = 2;

    private static final float BONEMEAL_CHANCE = 0.45F;

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public static final BooleanProperty RARE = BooleanProperty.create("rare");

    private final Settings settings;

    public VineCropBlock(Settings settings) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.GRASS)
            .strength(0.1F)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        this.settings = settings;
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(RARE, false));
    }

    public Settings settings() {
        return settings;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, RARE);
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
        VoxelShape[] shapes = settings.shapes();
        return shapes[Math.min(state.getValue(AGE), shapes.length - 1)];
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        return above.getBlock() instanceof VineArborBlock
            && above.getValue(VineArborBlock.SPECIES) == settings.species();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level,
                                  BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos)
            ? Blocks.AIR.defaultBlockState()
            : super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        if (state.getValue(AGE) < MAX_AGE) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            Item harvested = isRare(state) ? settings.rareHarvest().get() : settings.harvest().get();
            level.removeBlock(pos, false);
            Block.popResource(level, pos, new ItemStack(harvested, settings.harvestCount()));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private boolean isRare(BlockState state) {
        return settings.rareHarvest() != null && state.getValue(RARE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        
        if (random.nextFloat() < BONEMEAL_CHANCE) {
            advance(level, pos, state, random);
        }
    }

    public void advance(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        int age = state.getValue(AGE);
        if (age >= MAX_AGE) {
            return;
        }
        BlockState grown = state.setValue(AGE, age + 1);
        if (age + 1 == MAX_AGE && settings.rareHarvest() != null) {
            grown = grown.setValue(RARE, random.nextDouble() <= Config.rareVariantChance());
        }
        level.setBlock(pos, grown, 3);
    }

    public record Settings(VineSpecies species, Supplier<Item> harvest, @Nullable Supplier<Item> rareHarvest,
                           int harvestCount, VoxelShape[] shapes) {
    }
}
