package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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

public class AlfalfaCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPES = {
        Block.box(1, 0, 1, 15, 4, 15),
        Block.box(1, 0, 1, 15, 6, 15),
        Block.box(1, 0, 1, 15, 10, 15),
        Block.box(1, 0, 1, 15, 13, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(1, 0, 1, 15, 15, 15),
        Block.box(0, 0, 0, 16, 16, 16)
    };

    private final TagKey<Block> soil;
    private final Supplier<Item> seed;

    public AlfalfaCropBlock(TagKey<Block> soil, Supplier<Item> seed) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.CROP)
            .instabreak()
            .noCollission()
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava());
        this.soil = soil;
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seed.get();
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter world, BlockPos pos) {
        return state.is(soil);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).is(soil)
            && (world.getRawBrightness(pos, 0) >= 8 || world.canSeeSky(pos));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getAge(state)];
    }
}
