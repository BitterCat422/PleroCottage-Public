package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MintCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPES = {
        Block.box(4, 0, 4, 12, 6, 12),
        Block.box(3, 0, 3, 13, 7, 13),
        Block.box(3, 0, 3, 13, 9, 13),
        Block.box(3, 0, 3, 13, 11, 13),
        Block.box(2, 0, 2, 14, 12, 14),
        Block.box(1, 0, 1, 15, 12, 15),
        Block.box(1, 0, 1, 15, 13, 15),
        Block.box(1, 0, 1, 15, 16, 15)
    };

    private final Supplier<Item> seed;

    public MintCropBlock(Supplier<Item> seed) {
        super(BlockBehaviour.Properties.of()
            .sound(SoundType.CROP)
            .instabreak()
            .noCollission()
            .noOcclusion()
            .randomTicks()
            .pushReaction(PushReaction.DESTROY)
            .ignitedByLava());
        this.seed = seed;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return seed.get();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[getAge(state)];
    }
}
