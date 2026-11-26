package net.bittercat.plerocottage.blocks;

import javax.annotation.Nullable;

import net.bittercat.plerocottage.blockentities.BananaBlockEntity;
import net.bittercat.plerocottage.config.Config;
import net.bittercat.plerocottage.content.plants.Bananas;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BananaCrownBlock extends BaseEntityBlock {

    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 2);

    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, 1);

    public static final int FRUITING = 2;

    private static final int STEM_LENGTH = 3;

    private static final VoxelShape UNOPENED = Shapes.or(Block.box(3, 0, 3, 13, 10, 13),
        Block.box(-24, 9, -24, 40, 9.001, 40));
    private static final VoxelShape OPENED = Shapes.or(Block.box(3, 0, 3, 13, 10, 13),
        Block.box(-32, 9, -32, 48, 9.001, 48));

    public BananaCrownBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .sound(SoundType.BAMBOO_WOOD)
            .strength(3.0F)
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .isRedstoneConductor((state, level, pos) -> false));
        registerDefaultState(stateDefinition.any().setValue(BLOCKSTATE, 0).setValue(ANIMATION, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOCKSTATE, ANIMATION);
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
        return state.getValue(BLOCKSTATE) == 0 ? UNOPENED : OPENED;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(Bananas.SAPLING_ITEM.get());
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        for (int step = 1; step <= STEM_LENGTH; step++) {
            if (!level.getBlockState(pos.below(step)).is(Bananas.STALK.get())) {
                return;
            }
        }

        BlockPos flowerPos = pos.offset(0, -2, 1);
        BlockPos bunchPos = pos.offset(0, -1, 1);
        int stage = state.getValue(BLOCKSTATE);
        if (stage == 0) {
            if (random.nextDouble() < Config.leavesBlossomChance()
                && level.getBlockState(flowerPos).canBeReplaced()) {
                level.setBlock(pos, state.setValue(BLOCKSTATE, 1), 3);
                level.setBlock(flowerPos, Bananas.FLOWER.get().defaultBlockState(), 3);
            }
            return;
        }
        if (stage == 1 && level.getBlockState(bunchPos).canBeReplaced()
            && level.getBlockState(flowerPos).is(Bananas.FLOWER.get())) {
            if (random.nextDouble() < fruitChance()) {
                level.setBlock(bunchPos, Bananas.TREE_BUNCH.get().defaultBlockState(), 3);
                level.setBlock(pos, state.setValue(BLOCKSTATE, FRUITING), 3);
            }
            return;
        }
        
        if (!level.getBlockState(bunchPos).is(Bananas.TREE_BUNCH.get())
            || !level.getBlockState(flowerPos).is(Bananas.FLOWER.get())
            || random.nextDouble() >= fruitChance()) {
            return;
        }
        BlockState bunch = level.getBlockState(bunchPos);
        if (bunch.getValue(BananaTreeBunchBlock.BLOCKSTATE) >= BananaTreeBunchBlock.RIPE) {
            return;
        }
        level.setBlock(bunchPos, bunch.setValue(BananaTreeBunchBlock.BLOCKSTATE,
            bunch.getValue(BananaTreeBunchBlock.BLOCKSTATE) + 1), 3);
        BlockState flower = level.getBlockState(flowerPos);
        if (flower.getValue(BananaFlowerBlock.AGE) < BananaFlowerBlock.MAX_AGE) {
            level.setBlock(flowerPos, flower.setValue(BananaFlowerBlock.AGE,
                flower.getValue(BananaFlowerBlock.AGE) + 1), 3);
        }
    }

    private static double fruitChance() {
        return Config.leavesFruitChance() * 20.0D;
    }
}
