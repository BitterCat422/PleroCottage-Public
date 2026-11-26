package net.bittercat.plerocottage.blocks;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PeanutCropBlock extends CropBlock {

    private static final VoxelShape[] SHAPES = {
        Block.box(4, 0, 4, 12, 8, 12),
        Block.box(3, 0, 3, 13, 9, 13),
        Block.box(1, 0, 1, 15, 11, 15),
        Block.box(0, 0, 0, 16, 12, 16),
        Block.box(0, 0, 0, 16, 12, 16),
        Block.box(0, 0, 0, 16, 15, 16),
        Block.box(0, 0, 0, 16, 15, 16),
        Block.box(0, 0, 0, 16, 16, 16)
    };

    private final Supplier<Item> seed;

    public PeanutCropBlock(Supplier<Item> seed) {
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
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPES[getAge(state)];
    }
}
