package net.bittercat.plerocottage.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PineappleCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPES = {
        Block.box(3, 0, 3, 13, 10, 13),
        Block.box(3, 0, 3, 13, 11, 13),
        Block.box(3, 0, 3, 13, 11, 13),
        Block.box(3, 0, 3, 13, 14, 13),
        Block.box(3, 0, 3, 13, 14, 13),
        Block.box(3, 0, 3, 13, 17, 13),
        Block.box(3, 0, 3, 13, 21, 13),
        Block.box(3, 0, 3, 13, 23, 13)
    };

    public PineappleCropBlock() {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.CROP)
            .instabreak()
            .noCollission()
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava());
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return asItem();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return super.mayPlaceOn(state, world, pos) || state.is(BlockTags.SAND);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        if (world.getBlockState(pos.below()).is(BlockTags.SAND)) {
            return world.getRawBrightness(pos, 0) >= 8 || world.canSeeSky(pos);
        }
        return super.canSurvive(state, world, pos);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        
        if (world.getBlockState(pos.below()).is(BlockTags.SAND) && random.nextBoolean()) {
            return;
        }
        super.randomTick(state, world, pos, random);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getAge(state)];
    }
}
