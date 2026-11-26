package net.bittercat.plerocottage.blocks;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BananaFrondSaplingBlock extends BaseEntityBlock {

    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, 1);

    private static final float GROW_CHANCE = 0.35F;

    private static final int STEM_LENGTH = 4;

    private static final VoxelShape SHAPE = Shapes.or(Block.box(3, 0, 3, 13, 10, 13),
        Block.box(-16, 9, -16, 32, 9.001, 32));

    public BananaFrondSaplingBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .sound(SoundType.BAMBOO_WOOD)
            .strength(3.0F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ANIMATION);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BananaBlockEntity(pos, state);
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
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(BlockTags.DIRT);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level,
                                  BlockPos pos, BlockPos facingPos) {
        return state.canSurvive(level, pos) ? super.updateShape(state, facing, facingState, level, pos, facingPos)
            : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        grow(level, pos, random);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        ItemStack held = player.getItemInHand(hand);
        if (!held.is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            grow(level, pos, level.random);
            level.playSound(null, pos, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild) {
                held.shrink(1);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void grow(LevelAccessor level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() >= GROW_CHANCE) {
            return;
        }
        for (int step = 1; step <= STEM_LENGTH; step++) {
            if (!level.getBlockState(pos.above(step)).canBeReplaced()) {
                return;
            }
        }
        for (int step = 0; step < STEM_LENGTH; step++) {
            level.setBlock(pos.above(step), Bananas.STALK.get().defaultBlockState(), 3);
        }
        level.setBlock(pos.above(STEM_LENGTH), Bananas.CROWN.get().defaultBlockState(), 3);
    }
}
